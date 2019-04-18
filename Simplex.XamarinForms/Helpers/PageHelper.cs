using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Tmon.Simplex.Interfaces;
using Xamarin.Forms;
using Xamarin.Forms.Internals;

namespace Tmon.Simplex.XamarinForms.Helpers
{
    public static class PageHelper
    {
        public static Page Find(string id)
        {
            var mainPage = Application.Current.MainPage;
            var allPages = new List<Page> { mainPage }
                               .Concat(mainPage.Navigation.NavigationStack)
                               .Concat(mainPage.Navigation.ModalStack.SelectMany(x => x.Navigation.NavigationStack));

            var pageStack = new Stack<Page>(allPages);

            while (pageStack.Count > 0)
            {
                var page = pageStack.Pop();

                if (page.BindingContext is IActionSubscriber subscriber && subscriber.Id == id)
                    return page;

                if (page is TabbedPage tp)
                {
                    foreach (var c in tp.Children)
                        pageStack.Push(c);
                }
                else if (page is CarouselPage cp)
                {
                    foreach (var c in cp.Children)
                        pageStack.Push(c);
                }
                else if (page is MasterDetailPage mdp)
                {
                    pageStack.Push(mdp.Master);
                    pageStack.Push(mdp.Detail);
                }
            }

            return null;
        }

        public static void Unsubscribe(Page page)
        {
            GetChildrenAll(page).ForEach(p =>
            {
                if (p.BindingContext is IActionSubscriber subscriber)
                {
                    subscriber.Unsubscribe();

                    //GC 호출을 위해서 바인딩 강제 제거
                    p.RemoveBinding(BindableObject.BindingContextProperty);
                    p.BindingContext = null;
                }

                //직접 IActoinSubscriber인 경우
                if (p is IActionSubscriber pageSubscriber && p.BindingContext == null)
                {
                    pageSubscriber.Unsubscribe();
                }
            });
        }

        private static IEnumerable<Page> GetChildrenAll(Page root)
        {
            var children = new List<Page>();
            var pageStack = new Stack<Page>();
            pageStack.Push(root);

            //모든 자식 페이지를 찾음.
            while (pageStack.Count > 0)
            {
                var page = pageStack.Pop();
                children.Add(page);

                if (page is TabbedPage tp)
                {
                    foreach (var c in tp.Children)
                        pageStack.Push(c);
                }
                else if (page is CarouselPage cp)
                {
                    foreach (var c in cp.Children)
                        pageStack.Push(c);
                }
                else if (page is MasterDetailPage mdp)
                {
                    pageStack.Push(mdp.Master);
                    pageStack.Push(mdp.Detail);
                }
                else if (page is NavigationPage np)
                {
                    foreach (var c in np.Pages)
                        pageStack.Push(c);
                }
            }

            return children;
        }
    }
}
