# fedora-client Documentation Branch

This fedora-client documentation branch relies on: 
 * [GitHub Pages](http://pages.github.com/)
 * [Modernist Theme](http://orderedlist.github.com/modernist/)
 * maven-site-plugin and site-maven-plugin

# Setup

The Maven Site documentation is deployed to this branch's site/ directory.
With the exception of the contents of the site/ directory, the rest of the content 
started from the Modernist Theme.

## Updating the theme

To pull theme updates, add the theme repo as a remote:

```bash
git remote add modernist git@github.com:orderedlist/modernist.git
```

Thereafter, you can simply issue a `git pull`.

## CSS changes

The theme uses [Compass](http://compass-style.org/) for CSS authoring.

Assuming you have Ruby installed, install Compass:

```bash
$ bundle install

```

Then make your css changes to `sass/styles.scss` and:

```bash
$ compass compile

```

## Maven-generated documentation

The Maven generated documentation is done from the `master` branch with:
```bash
$ mvn site-deploy
```

You will need to manually update the gh-pages branch's index.html if you generate site 
documentation for a new version.


# License

This work is licensed under a [Creative Commons Attribution-ShareAlike 3.0 Unported License](http://creativecommons.org/licenses/by-sa/3.0/).

[remote "modernist"]
        url = git@github.com:orderedlist/modernist.git
        fetch = +refs/heads/*:refs/remotes/octopress/*