using System;
using System.Collections.Generic;
using System.Reactive.Concurrency;
using System.Text;

namespace Tmon.Simplex.Interfaces
{
    public interface IPlatform
    {
        IScheduler UIThreadScheduler { get; }
    }
}
