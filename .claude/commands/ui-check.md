Check the srvlog UI across all breakpoints using Chrome DevTools MCP. Verify sticky behavior, layout, and key components.

Run through this checklist automatically:

1. **Desktop (1440x900)** — emulate viewport, scroll to 0, screenshot, then scroll 400px and screenshot to verify sticky
2. **Tablet (768x1024)** — screenshot to check nav wrap, filter hidden
3. **Mobile (375x812)** — screenshot to check hamburger, compact pager

For each viewport, also run JS checks:
```js
{
  toolbarTop: toolbar.getBoundingClientRect().top,         // should be ~0 when scrolled
  theadTop: thead_th.getBoundingClientRect().top,          // should be toolbar_height when scrolled
  theadAboveBody: thead_top < first_tbody_top,             // must be true
  containerOverflow: computed overflow-x/y on #table-container, // must be visible/visible
  hasSearchWrapper: !!document.querySelector('.search-input-wrapper'),
}
```

Report issues clearly. For each breakpoint show: screenshot + any JS check failures.
At the end: **PASS** or **FAIL** with list of issues.

After the check, reset viewport to 1440x900 and scroll to top.
