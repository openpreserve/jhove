Documentation
=============
This is a quick start guide for anyone who wants to contribute to the JHOVE documentation. The official home of the JHOVE documentation is now here: http://jhove.openpreservation.org/. This is a [GitHub pages](https://help.github.com/articles/what-is-github-pages/) hosted web site meaning:
 - GitHub kindly host the site;
 - the documentation is part of the GitHub project so that anyone can contribute changes.

Pre-requisites
--------------
The bare minimum toolset needed to make changes to the documentation is a text editor and a knowledge of either [HTML](https://www.w3schools.com/html/) or [Markdown](https://daringfireball.net/projects/markdown/). The site also uses [Bootstrap](http://getbootstrap.com/) for look and feel but you can make changes without knowing any Bootstrap.

How it Works
------------
The source code for the website is held in the specifically named `gh-pages` branch of the git project. You can [see it on GitHub](https://github.com/openpreserve/jhove/tree/gh-pages), the home page for the website is the [`index.html` file in the root directory](https://github.com/openpreserve/jhove/blob/gh-pages/index.html). The folders starting with and underscore, `_data`, `_includes`, and `_layouts` provide site structure and page layouts and need only interest the curious. The directories `css` and `img` contain site stylesheets and images respectively. The other directories correspond to content paths on the website, for example  `documentation/index.html` is the site's http://jhove.openpreservation.org/documentation/ page.

### GitHub Pages, Jekyll and Markdown
If you're unfamiliar with GitHub Pages then their home page: https://pages.github.com/ is a good starting point. Under the hood it uses [Jekyll](https://jekyllrb.com/) to convert the GitHub `gh-pages` branch to the website. This conversion puts together pages and converts any Markdown files to HTML. If you're unfamiliar with Markdown, this file is written in it. It's a text-to-HTML conversion tool meaning that files can be written in a friendly plain text format, then converted to HTML. GitHub provide a comprehensive guide to [writing on GitHub](https://help.github.com/categories/writing-on-github/) which covers their own GitHub flavoured markdown. The [JHOVE beginners guide](http://jhove.openpreservation.org/getting-started/beginners/) is written in Markdown, you can see the markdown for the page alone [in the repository here](https://github.com/openpreserve/jhove/blob/gh-pages/getting-started/beginners/index.md) and the [raw source text file here](https://raw.githubusercontent.com/openpreserve/jhove/gh-pages/getting-started/beginners/index.md).

### DIY local site hosting
If you're more technically inclined you can serve a version of the site locally. This will update as you make edits and makes working with the documentation very quick. GitHub have already produced a good guide to this https://help.github.com/articles/setting-up-your-github-pages-site-locally-with-jekyll/.
