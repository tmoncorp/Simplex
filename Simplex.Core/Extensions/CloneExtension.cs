using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Tmon.Simplex.Exceptions;

namespace Tmon.Simplex.Extensions
{
    public static class CloneExtension
    {
        /// <summary>
        /// 깊은 복사를 수행한 결과를 리턴합니다.
        /// </summary>
        /// <typeparam name="T">복사할 데이터 타입</typeparam>
        /// <param name="source">복사할 데이터 원본</param>
        /// <returns>복사된 데이터</returns>
        public static T DeepClone<T>(this T source) 
        {
            try
            {
                return (T)DeepCloneObject(source);
            }
            catch (Exception ex)
            {
                throw new UnclonableTypeException(
                    $"{ex.Message} 깊은 복사에 실패하였습니다. 원본을 배출 하려면 prenventClone 옵션을 설정하세요. {source.GetType().FullName}", ex);
            }
        }

        /// <summary>
        /// 깊은 복사를 수행합니다.
        /// </summary>
        /// <typeparam name="T">복사할 데이터 타입</typeparam>
        /// <param name="source">복사할 원본 데이터</param>
        /// <param name="target">복사될 타겟 데이터</param>
        public static void DeepClone<T>(this T source, T target)
        {
            if (source == null || target == null)
                return;

            var sourceType = source.GetType();
            var targetType = target.GetType();

            //타입이 같지 않고, 상속관계에도 없으면 복사를 하지 않음
            if (sourceType != targetType && !targetType.IsSubclassOf(sourceType))
                return;

            //1. 원시자료형, 열거형이면 처리 안함
            if (sourceType.IsPrimitive || sourceType.IsEnum)
                return;

            //2. 문자열이면 처리 안함
            if (sourceType == typeof(string))
                return;

            //3. 배열형이면 요소별 복사 후 할당
            if (sourceType.IsArray)
                return;

            //4. 클래스 또는 구조체 형이면 내부 필드를 순환하며 복사 후 할당
            if (sourceType.IsClass || sourceType.IsValueType)
                CopyInstance(sourceType, source, target);
            else
                throw new ArgumentException("깊은 복사를 수행할 수 없는 타입입니다.");
        }

        /// <summary>
        /// 깊은 복사를 수행하여, 복제된 데이터를 리턴합니다.
        /// </summary>
        /// <param name="source">복사할 데이터 원본</param>
        /// <returns>복사된 데이터</returns>
        private static object DeepCloneObject(object source)
        {
            if (source == null)
                return null;

            var type = source.GetType();

            //1. 원시자료형, 열거형이면 할당
            if (type.IsPrimitive || type.IsEnum)
            {
                return source;
            }
            //2. 문자열이면 복사후 할당
            else if (type == typeof(string))
            {
                return string.Copy((string)source);
            }
            //3. 배열형이면 요소별 복사 후 할당
            else if (type.IsArray)
            {
                return CopyArray(type, source);
            }
            //4. 클래스 또는 구조체 형이면 내부 필드를 순환하며 복사 후 할당
            else if (type.IsClass || type.IsValueType)
            {
                try
                {
                    var target = Activator.CreateInstance(type, true);
                    CopyInstance(type, source, target);
                    return target;
                }
                catch(MissingMethodException mmex)
                {
                    Simplex.Logger?.Write($"{type.FullName} 필드 복사가 skip 되었습니다. {mmex.Message}");
                    //생성자가 없는 경우
                    return null;
                }
            }
            else
            {
                throw new ArgumentException("깊은 복사를 수행할 수 없는 타입입니다.");
            }
        }

        /// <summary>
        /// 배열형 데이터를 복사합니다.
        /// </summary>
        /// <param name="type">배열의 요소타입</param>
        /// <param name="source">배열 데이터</param>
        /// <returns>복사된 배열 데이터</returns>
        private static object CopyArray(Type type, object source)
        {
            var elementType = source.GetType().GetElementType();
            var array = (source as Array);
            var target = Array.CreateInstance(elementType, array.Length);

            for (int i = 0; i < array.Length; i++)
            {
                var value = array.GetValue(i);
                if (value != null)
                {
                    target.SetValue(DeepCloneObject(value), i);
                }
            }
            return target;
        }

        /// <summary>
        /// 클래스 타입의 객체를 복사합니다.
        /// </summary>
        /// <param name="type">복사할 데이터의 타입</param>
        /// <param name="source">복사할 원본 데이터</param>
        /// <param name="target">복사될 타겟 데이터</param>
        private static void CopyInstance(Type type, object source, object target)
        {
            var stack = new Stack<(Type currentType, BindingFlags bindingFlags)>();
            stack.Push((type, BindingFlags.Public | BindingFlags.NonPublic));

            while (stack.Any())
            {
                var (currentType, bindingFlags) = stack.Pop();

                //필드 copy
                var fieldInfos = currentType.GetFields(BindingFlags.Instance | bindingFlags);
                foreach (var fieldInfo in fieldInfos)
                {
                    var value = fieldInfo.GetValue(source);
                    if (value != null)
                        fieldInfo.SetValue(target, DeepCloneObject(value));
                }

                //부모 클래스의 필드의 private까지 복사 (부모 클래스에 property가 선언된 경우 백킹필드 값 복사)
                if (currentType.BaseType != null && currentType.BaseType != typeof(object))
                    stack.Push((currentType.BaseType, BindingFlags.NonPublic));
            }
        }
    }
}
