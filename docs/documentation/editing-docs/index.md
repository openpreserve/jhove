---
title: Editing the JHOVE documentation
nav_title: Editing docs
nav_order: 7
section: documentation
parent: Documentation
summary: How to contribute documentation updates, including metadata, images, and pull requests.
layout: page
---

# Editing the JHOVE docs

This guide explains how to propose documentation changes for JHOVE, from forking the repository through to opening a pull request.

## 1. Fork and clone

1. Fork the repository on GitHub (`openpreserve/jhove` -> your account).
1. Clone your fork locally.
1. Add the upstream remote so you can keep your fork up to date.

```bash
git clone git@github.com:<your-user>/jhove.git
cd jhove
git remote add upstream git@github.com:openpreserve/jhove.git
git fetch upstream
```

![Screenshot placeholder: GitHub page showing the Fork button for openpreserve/jhove](./images/01-fork-repo.png)

## 2. Create a working branch

Create a dedicated branch for your doc change.

```bash
git checkout -b docs/<short-topic-name>
```

Keep each branch focused on one logical change where possible.

![Screenshot placeholder: Terminal showing branch creation command and successful checkout](./images/02-create-branch.png)

## 3. Edit Markdown files

Most documentation pages are Markdown files under `docs/`.

Common section locations:

- `docs/getting-started/`
- `docs/documentation/`
- `docs/modules/`
- `docs/resources/`
- `docs/about/`

Guidelines:

- Use clear headings in a logical hierarchy (`#`, `##`, `###`).
- Keep paragraphs concise and task-focused.
- Use fenced code blocks for commands and examples.
- Use relative links for internal docs references where possible.

![Screenshot placeholder: VS Code editing a docs Markdown file under docs/documentation/](./images/03-edit-markdown.png)

## 4. Use the required front matter metadata

Each docs page should include YAML front matter fields like the following:

```yaml
---
title: Human readable page title
nav_title: Short label shown in navigation
nav_order: 1
section: documentation
parent: Documentation
summary: One-line description of the page purpose.
layout: page
---
```

Field notes:

- `title`: full page title.
- `nav_title`: shorter label used in breadcrumb/sidebar menus.
- `nav_order`: integer sort order among sibling pages.
- `section`: section key (`documentation`, `getting-started`, `modules`, etc.).
- `parent`: must exactly match the parent page `nav_title` (case and punctuation matter).
- `summary`: short description used by section landing and maintenance tooling.
- `layout`: use `page` for standard docs pages.

If a page does not appear in the expected place in navigation, first check `section`, `parent`, and `nav_order`.

![Screenshot placeholder: YAML front matter block highlighted with title/nav_title/nav_order/section/parent/summary/layout](./images/04-front-matter.png)

## 5. Add and reference images

Store images near the page they belong to.

Example for this page:

- page: `docs/documentation/editing-docs/index.md`
- images: `docs/documentation/editing-docs/images/`

Reference with relative paths:

```md
![Alt text describing the screenshot](./images/example.png)
```

Image tips:

- Prefer PNG for screenshots.
- Keep filenames lowercase with hyphens.
- Use descriptive alt text that explains what the image shows.
- Avoid oversized images when a cropped screenshot communicates better.

![Screenshot placeholder: File explorer showing images stored in a page-local images folder](./images/05-images-location.png)

## 6. Review your change before commit

1. Check spelling, links, and heading structure.
1. Confirm front matter values are correct.
1. Ensure new images render and paths resolve.

```bash
git status
git diff
```

If your environment supports local site preview, run a local docs build and check the updated page in a browser before pushing.

![Screenshot placeholder: Browser preview of updated docs page with sidebar and breadcrumbs visible](./images/06-local-preview.png)

## 7. Commit and push

```bash
git add docs/
git commit -m "docs: add/update <topic>"
git push origin docs/<short-topic-name>
```

Prefer small, focused commits with clear messages.

## 8. Open a pull request

When opening a PR:

- Provide a concise summary of what changed.
- Explain why the change is needed.
- Include screenshots for visual changes.
- Link any related issue/ticket if available.

PR checklist:

- [ ] Navigation metadata correct (`section`, `parent`, `nav_order`)
- [ ] Links and image paths work
- [ ] Wording and formatting reviewed
- [ ] Scope limited to the intended doc change

![Screenshot placeholder: GitHub pull request page showing title, description, and changed files](./images/07-open-pr.png)

## 9. Keep your fork up to date

Before your next change:

```bash
git checkout main
git fetch upstream
git merge upstream/main
git push origin main
```

This reduces merge conflicts and keeps your branches current.

## Quick troubleshooting

### Page missing from sidebar or breadcrumbs

- Verify `section` and `parent` values.
- Ensure `parent` exactly matches the target parent `nav_title`.
- Check `nav_order` values among siblings.

### Broken image

- Check relative path from the current page.
- Confirm file exists and case matches exactly.

### Unexpected layout

- Confirm `layout: page` is present.

For large documentation restructures, split work into multiple PRs to simplify review.
