Purpose
----------------------

This repo contains a minimal ClojureScript project which shows how to mix:

* [cemerick/clojurescript.test]  - a unittest framework
* with the `:optimizations :none` setting in cljsbuild (see project.clj) and
* use of `lein cljsbuild auto <testname>`

This combination results a fast unittest workflow. Compile times are short. Iterative debugging is fast.


The Problem It Solves
----------------------

At the time of writing, [cemerick/clojurescript.test]  doesn't work with `:optimizations :none`.
I tried, it broke, I cursed, I looked into it more wondering what I had done wrong, and pretty quickly
realised that it could never work as things stood.

I retreated back to using `:optimizations :whitespace` which did work, thankfully, but
I found my workflow uncomfortable.
Building a big test.js each time meant my compiles took too long, and this
made iterative development
cycles feel slow and clunky. Flow was broken.  No, not good enough!!  I needed the near-instant compile
time you get by combining `:optimizations :none` and `lein cljsbuild auto <testname>`

So I figured out how to make it happen. And this repo shows how.

I've also created a `test.html` - a browser-based unittest runner which allows me to
debug unittests using chrome dev-tools.  You can forget the command line
and just refresh a browser page to see your unittest results.

Having said that, if the command line is your thing, or you need to automate
deployments etc, this repo also shows you how to mix `:optimizations :none`
with `phantomjs`.



How Does It work?
----------------------

There's a pretty simple hack at the center of this.

First, you should look in `test.html`. Read the explanation in there.

Then, look at `test/bin/runner-none.js` to see how the same thing is achieved
in phantomjs. You'll see it is a bit more complicated, and there's not as much
explanation in that file, but armed with what you read in `test.html`
you'll figure it out.



Just Tell Me What To Do!
----------------------

Clone this repo:

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

Then, load `test.html` into a browser. Bingo! You should see the output from [cemerick/clojurescript.test].

Phantomjs
--------------------

If the browser is not your thing, or you need to automate deployments etc, you can use phantomjs on the command line.


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


You would use phantomjs like this:
```
phantomjs test/bin/runner-none.js  compiled/test  compiled/test.js
```

Notes:
* the first parameter is a custom `runner script` which understand `:optimizations :none`, and is provided in this repo [here]
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



Do you have a runner for Node?
----------------------

No, sorry I don't need one just yet, so I haven't done it. But if you wanted to try, its going to be like the phantomjs runner, but simpler.




Copyright and license
-------------------

Copyright © 2014 Mike Thompson

Licensed under the EPL (see the file epl.html).


[gist]:https://gist.github.com/mike-thompson-day8/8a87349cf69697bfcd64
[figwheel]:https://github.com/bhauman/lein-figwheel
[this problem]:https://github.com/ariya/phantomjs/issues/10845#issuecomment-14994358
[cemerick/clojurescript.test]:https://github.com/cemerick/clojurescript.test
[here]:https://github.com/mike-thompson-day8/cljsbuild-none-test-seed/blob/master/test/bin/runner-none.js


