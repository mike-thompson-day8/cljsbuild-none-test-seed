Purpose
----------------------

This repo contains a minimal ClojureScript project which shows how to mix:

* emerick.cljs.test     (a unittest framework)
* with the `:optimizations :none` setting in cljsbuild (see project.clj) and
* use of `lein cljsbuild auto <testname>`

This combination results a fast unittest workflow. Compile times are short. Iterative debugging is fast.


The Problem It Solves
----------------------

Previously, I've had to use `:optimizations :whitespace`  or `:simple` when transpiling (cljsbuild-ing)
unittests, which I've found uncomfortable because it takes too long.
Iterative development was slow. Flow gets broken.  No, not good enough.  This repo is a solution.

I've also created a `test.html` - a browser-based unittest runner which allows me to
debug unittests using chrome dev-tools.  Also, I wanted the option of not using
the commandline, and instead just refreshing a browser page to see my unittest results.

Having said that, if the commandline is your thing, or you need to automate
deployments etc, this repo also shows you how to mix `:optimizationss :none`
with phantomjs.



How Does It work?
----------------------

There's a pretty simple hack at the center of this which makes it all possible.

First, you should look in `test.html`. Read the explanation in there.

Then, look at `test/bin/runner-none.js` to see how the same thing is achieved
in phantomjs. You'll see its a bit more complicated, and there's not much
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
Or, using the alias (see the end of project.clj):
```
lein auto-test
```

You might initially see a bunch of dependencies being downloaded, and that could take a mintue the first time. Wait till you see string `Compiling ClojureScript.`

Then, load `test.html` into a browser. Bingo! You should see the output from emerick.cljs.test.

Or, at the commandline (in the root directory) you can try this:
```
phantomjs test/bin/runner-none.js  compiled/test/goog/ compiled/test.js
```


Now Experiement
----------------------

Pull out you favorite editor, and make a change to a test (a cljs file in `test` directory). Save it. And watch as `lein cljsbuild auto test` performs a deliciously fast recompile. Then refresh `test.html` to see if you tests still pass.

Introduce an error into your tests.  Or perhaps a rogue  `throw`.  See how it shows up when you refresh `test.html`.  Set breakpoints in chrome dev tools. Etc.


Phantomjs Doesn't Shutdown?
----------------------

If you have an NVidia card, it might be [this problem] which also bit me.  How bizare.



Now Introduce Figwheel
----------------------


If you introduce [figwheel], you won't  even have to go through the arduous process of clicking the refresh button on `test.hrml`


What If I'm Using Speclj?
----------------------

The technique used here will work just fine.  Here is a [gist] to get you going.



[gist]:http://XXXXXXX.XXXX/
[figwheel]:https://github.com/bhauman/lein-figwheel
[this problem]:https://github.com/ariya/phantomjs/issues/10845#issuecomment-14994358



Do you have a runner for Node?
----------------------

No, sorry I don't need one just yet, so I haven't done it. But if you wanted to try, its going to be like the phantomjs runner, but simpler.




Copyright and license
-------------------

Copyright © 2014 Mike Thompson

Licensed under the EPL (see the file epl.html).
