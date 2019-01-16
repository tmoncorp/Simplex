using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;

namespace Tmon.Simplex.Extensions
{
    public static class ReflectionExtension
    {
        internal static FieldInfo GetBackingField(this PropertyInfo propertyInfo, Type source, BindingFlags bindingFlags)
        {
            FieldInfo fInfo = null;
            var typeStack = new Stack<Type>();
            typeStack.Push(source);

            while (typeStack.Any())
            {
                var type = typeStack.Pop();
                fInfo = type.GetField($"<{propertyInfo.Name}>k__BackingField", bindingFlags);

                if (fInfo != null || type.BaseType == null || type.BaseType == typeof(object))
                    break;

                typeStack.Push(type.BaseType);
            }
            return fInfo;
        }
    }
}
