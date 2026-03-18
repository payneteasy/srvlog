# SRVLOG — Claude Code контекст

## Проект
Wicket + Spring Boot + Tailwind UI. Лог-коллектор.

## Dev workflow

### Изменения HTML/CSS
- **Оба типа файлов требуют перезапуска `StartUI`.**
- CSS (`main.css` в `src/main/webapp/css/`) — Wicket версионирует ресурсы: хэш URL вычисляется при старте. Новый CSS не попадёт в браузер до перезапуска.
- HTML (`.html` в `src/main/java/`) — Wicket кэширует markup в памяти JVM при старте.
- **Workflow:** отредактировать файл(ы) → попросить перезапустить `StartUI` → проверить в браузере

### Критические CSS-зависимости (легко сломать)
- **`overflow` на `#table-container`** — если поставить `overflow-x: auto` или `overflow-y: hidden/auto`,
  `position: sticky` на `thead th` перестаёт работать (sticky становится относительно контейнера, не страницы)
- **`position: sticky` на `<thead>`** — НЕ работает с большим `top` в Chrome: позиционирует thead ниже tbody.
  Правильно: `position: sticky` на `<th>` ячейках
- **`overflow-y: clip`** — Chrome вычисляет его как `hidden` при `overflow-x: auto` на том же элементе → создаёт scroll context → ломает sticky
- **Toolbar `top`** — navbar НЕ фиксирован, скроллится вместе со страницей. Toolbar sticky `top: 0`

## UI чеклист (запускать после любых CSS/HTML изменений)

Используй `/ui-check` или вручную:

```
Desktop (1440px):
  [ ] Navbar: все ссылки видны, hamburger скрыт
  [ ] Filter sidebar: справа, всегда видна
  [ ] Filter toggle btn: скрыт
  [ ] Sticky toolbar: при скролле прилипает к top:0
  [ ] Sticky thead: прилипает сразу под toolbar
  [ ] Все кнопки toolbar (search icon, 25/50/100, First/Prev/Next): одной высоты

Tablet (768px):
  [ ] Navbar: все ссылки видны (wrap)
  [ ] Filter sidebar: скрыта, filter toggle btn виден
  [ ] Filter drawer: открывается по клику
  [ ] Backdrop: появляется за drawer

Mobile (375px):
  [ ] Navbar: hamburger, меню раскрывается
  [ ] Pager: First скрыт, Previous → ‹, Next → ›
  [ ] Table: колонки не выезжают за экран
```

## Стек
- Wicket 6.x + Spring + Spring Security
- Tailwind (compiled, не все классы попадают в билд — использовать explicit CSS в `main.css`)
- JS: минимум, только toggle классов и расчёт sticky offsets
- CSS: `main.css` — основной файл кастомных стилей, `tailwind.css` — compiled

## Файлы
- `srvlog-web/src/main/java/com/payneteasy/srvlog/wicket/page/` — HTML templates
- `srvlog-web/src/main/java/com/payneteasy/srvlog/wicket/page/css/` — CSS/Tailwind
- `.run/StartUI.run.xml` — конфиг запуска (dev mode включён)
