# shadow-cljs workflow

```
yarn

# this process will become a CLJS-REPL
shadow-cljs --build main --dev

# second webpack process to webpacky things
./node_modules/.bin/webpack-dev-server --config webpack.dev.coffee

open http://localhost:8080/dev.html
```

I generated the `target/dev.html` once and then added the required changes manually. Moved everything to `public`, you don't need to do that. Just configure a different `:public-dir` in the `shadow-cjls.edn`.

Once you have the `dev.html` open in a browser you can use the `shadow-cljs` process as REPL. Try `(js/alert "foo")`.

`shadow-cljs.edn` configures the `main` build above.

```
[{:id :main
  :target :browser

  :public-dir "public/js"
  :public-path "/js"

  :modules
  {:main
   {:entries [client.main]}}

  :devtools
  {:before-load client.main/stop
   :after-load client.main/start}}]
```

`:devtools` `:before-load` will be called before any code is reloaded, `:after-load` will be called once all code is loaded. To get things started the HTML will call `client.main/init` once and that will call `start`. You can use `init` to do things you only want done once. After that it is just `stop` -> `start`.

The HMR and REPL code is injected automatically for `--dev`.


```
shadow-cljs --build main --once
```

`--once` will compile in `:dev` mode but without REPL or HMR


```
shadow-cljs --build main --release
```

`--release` will compile with `:advanced` compilation.

The generated file is always `public/js/main.js` so no changes to the HTML are required to try a `--release` build.