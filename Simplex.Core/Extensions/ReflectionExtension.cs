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

        internal static void SetValueDeeply(this PropertyInfo propertyInfo, object obj, object value)
        {
            if (propertyInfo.CanWrite)
            {
                propertyInfo.SetValue(obj, value);
            }
            else
            {
                //set메소드가 없는 경우, backing필드를 검색하여 fieldInfo 생성
                var bindingFlag = BindingFlags.Instance | BindingFlags.NonPublic;
                var fieldPropertyInfo = propertyInfo.GetBackingField(obj.GetType(), bindingFlag);

                //백 필드를 못 찾으면 채널생성 skip
                if (fieldPropertyInfo == null)
                    return;

                fieldPropertyInfo.SetValue(obj, value);
            }
        }
    }
}
