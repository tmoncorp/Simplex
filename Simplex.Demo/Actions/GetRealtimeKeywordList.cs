using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Reactive.Linq;
using System.Reactive.Threading.Tasks;
using System.Text;
using Tmon.Simplex.Actions;
using Newtonsoft.Json;
using Tmon.Simplex.Demo.Models;

namespace Tmon.Simplex.Demo.Actions
{
    public class GetRealtimeKeywordList : AbstractAction<IEnumerable<PopularKeyword>>
    {
        public override IObservable<IEnumerable<PopularKeyword>> Process()
        {
            var client = new HttpClient();
            
                return client
                    .GetAsync("http://search.ticketmonster.co.kr/api/direct/v1/searchextra2api/api/search/popularkeyword/v2/realtime")
                    .ToObservable()
                    .SelectMany(x =>
                    {
                        return x.Content
                            .ReadAsStringAsync()
                            .ToObservable()
                            .Select(content => JsonConvert.DeserializeObject<PopularKeywordList>(content).Data.List);
                    });
                    

            
        }
    }
}
