using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Tmon.Simplex.Exceptions
{
    public class UnhandledErrorException : Exception
    {
        /// <summary>
        /// Creates a new instance of <c>UnhandledErrorException</c>.
        /// </summary>
        public UnhandledErrorException()
        {
        }

        /// <summary>
        /// Creates a new instance of <c>UnhandledErrorException</c>.
        /// </summary>
        /// <param name="message">
        /// The exception message.
        /// </param>
        public UnhandledErrorException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Creates a new instance of <c>UnhandledErrorException</c>.
        /// </summary>
        /// <param name="message">
        /// The exception message.
        /// </param>
        /// <param name="innerException">
        /// The exception that caused this exception.
        /// </param>
        public UnhandledErrorException(string message, Exception innerException)
            : base(message, innerException)
        {
        }

    }
}
