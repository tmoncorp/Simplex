using System;
using System.Collections.Generic;
using System.Text;

namespace Tmon.Simplex.Actions
{
    public interface IActionBinderSet
    {
        TActionBinderSet GetOrAdd<TActionBinderSet>()
            where TActionBinderSet : IActionBinderSet, new();
    }
}
