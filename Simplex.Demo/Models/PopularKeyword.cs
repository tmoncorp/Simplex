using System;
using System.Collections.Generic;
using System.Text;

namespace Tmon.Simplex.Demo.Models
{
    public class PopularKeywordList
    {
        public Data Data { get; set; }

        public string HttpStatus { get; set; }

        public int HttpCode { get; set; }
    }

    public class Data
    {
        public IEnumerable<PopularKeyword> List { get; set; }

        public DateTime UpdateDate { get; set; }
    }

    public class PopularKeyword
    {
        public string Keyword { get; set; }

        public string MobileImagePath { get; set; }

        public int Rank { get; set; }

        public string RankLog { get; set; }

        public string State { get; set; }

        public double Weight { get; set; }
    }
}
