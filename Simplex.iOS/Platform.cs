using System;
using System.Collections.Generic;
using System.Linq;
using System.Reactive.Concurrency;
using System.Text;

using Foundation;
using Tmon.Simplex.Concurrency;
using Tmon.Simplex.Interfaces;
using UIKit;

namespace Tmon.Simplex
{
    public class Platform : IPlatform
    {
        private Platform() { }

        public static IPlatform Current { get; } = new Platform();

        public IScheduler UIThreadScheduler { get; }
            = new UIThreadScheduler();
    }
}
