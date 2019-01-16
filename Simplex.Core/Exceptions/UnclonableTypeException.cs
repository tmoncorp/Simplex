using System;
using System.Collections.Generic;
using System.Text;

namespace Tmon.Simplex.Exceptions
{
    public class UnclonableTypeException : Exception
    {
        public UnclonableTypeException()
        {
        }

        public UnclonableTypeException(string message)
            : base(message)
        {
        }

        public UnclonableTypeException(string message, Exception innerException)
            : base(message, innerException)
        {
        }
    }
}
