using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Text;
using System.Threading.Tasks;
using Tmon.Simplex.Interfaces;

namespace Tmon.Simplex
{
    public class Platform : IPlatform
    {
        private Platform() { }

        public static IPlatform Current { get; } = new Platform();

        public IScheduler UIThreadScheduler
            => Tmon.Simplex.Concurrency.UIThreadScheduler.Current;
    }
}
