using System;
using System.Collections.Generic;
using System.Runtime.CompilerServices;
using System.Text;

namespace Tmon.Simplex.Actions
{
    /// <summary>
    /// 액션 래핑 클래스를 그룹화형 관리할 인터페이스
    /// </summary>
    public interface IActionBinderSet
    {
        /// <summary>
        /// 액션 래핑 클래스를 조회 (등록된 액션 래핑 클래스가 존재하지 않으면  신규로 등록 함)
        /// </summary>
        /// <typeparam name="TActionBinderSet"></typeparam>
        /// <returns></returns>
        TActionBinderSet GetOrAdd<TActionBinderSet>()
            where TActionBinderSet : IActionBinderSet, new();

        /// <summary>
        /// 단위작업 바인더를 조회합니다. 단위작업이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
        /// </summary>
        /// <typeparam name="TAction">단위작업의 타입</typeparam>
        /// <typeparam name="TParam">단위작업의 매개변수 타입</typeparam>
        /// <typeparam name="TResult">단위작업의 결과 반환 타입</typeparam>
        /// <param name="key">단위작업의 등록 키</param>
        /// <returns>단위작업 바인더 인스턴스</returns>
        IActionBinder<TAction, TParam, TResult> GetOrAdd<TAction, TParam, TResult>([CallerMemberName] string actionBinderKey = "")
           where TAction : class, IAction<TParam, TResult>, new();

        /// <summary>
        /// 단위작업 바인더를 조회합니다. 단위작업이 등록되어 있지 않으면 새롭게 등록하고 해당 객체를 리턴합니다.
        /// </summary>
        /// <typeparam name="TAction">단위작업의 타입</typeparam>
        /// <typeparam name="TResult">단위작업의 결과 반환타입</typeparam>
        /// <param name="key">단위작업의 등록 키</param>
        /// <returns>단위작업 바인더 인스턴스</returns>
        IActionBinder<TAction, TResult> GetOrAdd<TAction, TResult>([CallerMemberName] string actionBinderKey = "")
            where TAction : class, IAction<TResult>, new();
    }
}
