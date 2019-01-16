using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Tmon.Simplex.Channels;
using Tmon.Simplex.Comparers;

namespace Tmon.Simplex.Extensions
{
    internal static class ChannelExtension
    {
        internal static IChannel Zip(this IChannel[] channels)
        {
            return new ChannelZip(channels);
        }

        internal static IChannel[] Extract(this IChannel channel)
        {
            if (channel is ChannelZip zip)
                return zip.Channels.ToArray();
            else
                return new IChannel[] { channel };
        }

        internal static bool Contains(this IChannel source, IChannel value)
        {
            return source.Extract().Intersect(value.Extract(), new ChannelComparer()).Any();
        }
    }
}
