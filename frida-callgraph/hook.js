'use strict';

const config = {
  classPrefixes: ['com.junkfood.seal.'],
  skipClassContains: ['$$ExternalSyntheticLambda', '$serializer', '$Companion$WhenMappings'],
  maxClasses: 260,
  maxMethodsPerClass: 80,
  maxArgLength: 120
};

function now() {
  return new Date().toISOString();
}

function safeString(value) {
  try {
    if (value === null || value === undefined) {
      return String(value);
    }
    const text = value.toString();
    return text.length > config.maxArgLength ? text.substring(0, config.maxArgLength) + '...' : text;
  } catch (e) {
    return '<unprintable>';
  }
}

function shouldHookClass(name) {
  if (!config.classPrefixes.some(prefix => name.indexOf(prefix) === 0)) {
    return false;
  }
  return !config.skipClassContains.some(part => name.indexOf(part) >= 0);
}

function emit(event) {
  console.log(JSON.stringify(event));
}

function uniqueMethodNames(clazz) {
  const names = {};
  const methods = clazz.class.getDeclaredMethods();
  for (let i = 0; i < methods.length; i++) {
    const name = methods[i].getName();
    if (name.indexOf('$') >= 0) {
      continue;
    }
    names[name] = true;
  }
  return Object.keys(names).slice(0, config.maxMethodsPerClass);
}

function hookMethod(clazz, className, methodName) {
  const overloads = clazz[methodName].overloads;
  overloads.forEach(function (overload) {
    const signature = className + '.' + methodName + '(' + overload.argumentTypes.map(t => t.name).join(',') + ')';
    overload.implementation = function () {
      const args = [];
      for (let i = 0; i < arguments.length; i++) {
        args.push(safeString(arguments[i]));
      }
      emit({
        type: 'enter',
        time: now(),
        thread: Java.use('java.lang.Thread').currentThread().getName().toString(),
        className: className,
        methodName: methodName,
        signature: signature,
        args: args
      });
      let result;
      try {
        result = overload.apply(this, arguments);
        emit({
          type: 'exit',
          time: now(),
          className: className,
          methodName: methodName,
          signature: signature,
          result: safeString(result)
        });
        return result;
      } catch (e) {
        emit({
          type: 'throw',
          time: now(),
          className: className,
          methodName: methodName,
          signature: signature,
          error: safeString(e)
        });
        throw e;
      }
    };
  });
}

Java.perform(function () {
  const loaded = Java.enumerateLoadedClassesSync()
    .filter(shouldHookClass)
    .slice(0, config.maxClasses);

  emit({
    type: 'meta',
    time: now(),
    message: 'frida hook started',
    classCount: loaded.length,
    prefixes: config.classPrefixes
  });

  loaded.forEach(function (className) {
    try {
      const clazz = Java.use(className);
      const methods = uniqueMethodNames(clazz);
      methods.forEach(function (methodName) {
        try {
          if (clazz[methodName] && clazz[methodName].overloads) {
            hookMethod(clazz, className, methodName);
          }
        } catch (e) {
          emit({ type: 'hook_error', className: className, methodName: methodName, error: safeString(e) });
        }
      });
      emit({ type: 'class_hooked', className: className, methodCount: methods.length });
    } catch (e) {
      emit({ type: 'class_error', className: className, error: safeString(e) });
    }
  });
});
