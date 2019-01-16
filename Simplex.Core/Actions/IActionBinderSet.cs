using System;
using System.Collections.Generic;
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
    }
}
