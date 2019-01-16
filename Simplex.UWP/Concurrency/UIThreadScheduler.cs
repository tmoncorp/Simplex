using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Text;
using System.Threading.Tasks;

namespace Tmon.Simplex.Concurrency
{
    public static class UIThreadScheduler
    {
        public static IScheduler Current => CoreDispatcherScheduler.Current;
    }
}
