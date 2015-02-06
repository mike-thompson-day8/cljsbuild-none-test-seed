Purpose
---------------

This repo contains a minimal ClojureScript project showing how to mix:

* [cemerick/clojurescript.test]  - a unittest framework
* with the `:optimizations :none` setting in cljsbuild (see project.clj) and
* use of `lein cljsbuild auto <testname>`

This combination results a fast unittest workflow. Compile times are short. Iterative debugging is fast.


The Problem It Solves
----------------------

At the time of writing, [cemerick/clojurescript.test]  doesn't work with `:optimizations :none`.
I tried, it broke, I cursed, I looked into it more wondering what I had done wrong, and quickly
realised that it could never work as things stood.

I retreated back to using `:optimizations :whitespace` which did work, thankfully, but
I found my workflow uncomfortable.

Building a big `test.js` each time meant my compiles took too long, and this
made iterative development
cycles feel slow and clunky. Flow was broken.  I needed the near-instant compile
time that comes from combining `:optimizations :none` and `lein cljsbuild auto <testname>`

So, I figured out how to make it happen. And this repo shows how.

What This Repo Supplies
-----------------------

The key assets are:

* a browser-based unittest runner `test.html` (found in this root directory). It will allow you to
debug unittests using chrome dev-tools.  You can forget the command line
and just refresh a browser page to see your unittest results.

* a unittest runner script for `phantom.js` - found at `test/bin/runner-none.js`

* a unittest runner script for `nodejs` - found at `test-node/bin/runner-none.js`


Why is :none different?
-----------------------

When the setting for `:optimization` is one of `:simple` `:whitespace` or `:advanced`, cljsbuild will put *all* the javascript into a single, large ".js" file (nominated via `:output-to` in your project.clj).

This can take a while at compile time, but this one-file outcome certainly makes it easy at run time.

You just load this one file into the browser (think \<script\>) or nodejs (think command line), and presto, everything is there.

This repo shows how to handle the more difficult run-time situation created by `:optimizations :none` because you don't end up with one large javascript file, but rather **many small javascript files and some dependency information about them**.

At runtime, you have to stitch these files together.

For your typical application, this "stitching" is not difficult.  You use the Google Closure runtime, `goog`, to `require` a single, known root namespace and `goog` automatically handles the rest, by triggering a cascade of further `requires` for any upstream namespaces (and then iteratively their upstream requires, etc). One call to `goog.require()` and you are nicely stitched.

But in the case of unittests, where there's a flat namespace structure (many unknown roots, one for each cljs file in your tests directory), it is all a bit more of a challenge. Until now.



So How Does The Solution Work?
----------------------

Turns out there's a pretty simple hack at the center of this.

To start, read the long comment in `test.html`.

Then, look at `test/bin/runner-none.js` to see how the same thing is achieved
in phantomjs. You'll see it is a bit more complicated, and more sparsely
documented (never a good combination), but armed with what's in `test.html`
you'll figure it out.

Node is a different beast again, which requires a different test target in the `project.clj`
and a different runner script in `test-node/bin/runner-none.js`


Just Tell Me What To Do!
----------------------

Okay fine.  First, clone this repo:

```sh
git clone https://github.com/mike-thompson-day8/cljsbuild-none-test-seed.git
```

Step down into the project folder just created:

```sh
cd cljsbuild-none-test-seed
```

Start auto compilation (if you change a file and save it, a recompile will happen automatically)


```
lein cljsbuild auto test
```
Or, instead of the command above, you could use the alias (see the end of project.clj):
```
lein auto-test
```

You might initially see a bunch of dependencies being downloaded, and that could take a minute the first time. Wait till you see `Compiling ClojureScript.`

Then, load `test.html` into a browser. Bingo! You should see the output from the [cemerick/clojurescript.test] runner.


Phantomjs
--------------------

If the browser is not your thing, you can use phantomjs on the command line.


Imagine for a minute that your project.clj contained this cljsbuild spec involving `:optimizations :none`:
```
:cljsbuild {
  :builds [{:id           "test"
            :source-paths ["src" "test"]
            :compiler     {:output-to     "compiled/test.js"      ;; <--  we use this
                           :source-map    "compiled/test.js.map"
                           :output-dir    "compiled/test"         ;; <--  and this
                           :optimizations :none
                           :pretty-print  true}}
```


You would use phantomjs at the command line like this:
```
phantomjs test/bin/runner-none.js  compiled/test  compiled/test.js
```

Notes:
* the first parameter is a custom `runner script` which understands `:optimizations :none`, and is provided [in this repo here]
* the 2nd and 3rd parameters are the cljsbuild values for `:output-dir` and `:output-to` respectively.

As an exercise, go to root folder of this repo and try the command line given above.


BTW, if you find that phantomjs doesn't quit AND you have an NVidia card, it might be [this problem]. It bit me too. How bizarre.


Now Experiment
----------------------

Pull out your favorite editor, and make a change to a test (a cljs file in `test` directory). Save it. And watch as `lein cljsbuild auto test` performs a deliciously fast recompile. Then refresh `test.html` to see if your tests still pass.

Introduce an error into your tests.  Or perhaps a rogue  `throw`.  See how it shows up when you refresh `test.html`.  Set breakpoints in chrome dev tools. Etc.



Now Introduce Figwheel
----------------------


If you introduce [figwheel], you won't  even have to go through the arduous process of clicking the refresh button on `test.html`


What If I'm Using Specljs?
----------------------

The technique used here will work just fine.  Here is a [gist] to get you going.



What About Node?
----------------------

You'll notice that `test.html` and `phantomjs` shared the one build target in the project.clj.  In effect, they could both use `test.js`.


But Node is a different beast:

* You'll see in the `project.clj` that there's a seperate `test-node` build target which includes the line `:target :nodejs`.
* Also look at the last line in `test-node/core_test.cljs`.

So, to make it happen:
```
lein cljsbuild auto test-node
```

And then run the node tests (you have node installed, right?):
```
node  test-node/bin/runner-none.js  compiled/test-node  compiled/test-node.js
```

As with phantomjs (described above):
* the first parameter is a custom `runner script` which understands `:optimizations :none`, and
* the 2nd and 3rd parameters are the cljsbuild values for `:output-dir` and `:output-to` respectively.


Copyright and license
-------------------

Copyright © 2014 Mike Thompson

Licensed under the EPL (see the file epl.html).


[gist]:https://gist.github.com/mike-thompson-day8/8a87349cf69697bfcd64
[figwheel]:https://github.com/bhauman/lein-figwheel
[this problem]:https://github.com/ariya/phantomjs/issues/10845#issuecomment-14994358
[cemerick/clojurescript.test]:https://github.com/cemerick/clojurescript.test
[in this repo here]:https://github.com/mike-thompson-day8/cljsbuild-none-test-seed/blob/master/test/bin/runner-none.js


