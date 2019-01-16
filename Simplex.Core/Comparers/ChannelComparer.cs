using System;
using System.Collections.Generic;
using System.Text;
using Tmon.Simplex.Channels;

namespace Tmon.Simplex.Comparers
{
    internal class ChannelComparer : IEqualityComparer<IChannel>
    {
        public bool Equals(IChannel x, IChannel y)
        {
            return x.Id == y.Id;
        }

        public int GetHashCode(IChannel obj)
        {
            return obj.Id.GetHashCode();
        }
    }
}
