# mazes

I'm working through Jamis Buck's [Mazes for
Programmers](https://pragprog.com/book/jbmaze/mazes-for-programmers).

I'm a Clojure newb, so this is not likely to be fantastic code.

## Examples

Fire up your REPL:

    $ lein repl

To print a 10-by-10 grid with no links between any cells:

    mazes.core=> (require '[mazes.repr.ascii :refer [print-grid]])
    mazes.core=> (print-grid (make-grid 3 3))

That'll give you something like this:

    +---+---+---+
    |   |   |   |
    +---+---+---+
    |   |   |   |
    +---+---+---+
    |   |   |   |
    +---+---+---+

To generate a maze using Wilson's algorithm on a 5-by-10 grid:

    mazes.core=> (require '[mazes.alg.wilsons :as wilsons])
    mazes.core=> (print-grid (wilsons/on (make-grid 5 10)))

That'll give you something slightly (but only slightly) more impressive:

    +---+---+---+---+---+---+---+---+---+---+
    |           |   |   |   |       |       |
    +---+   +---+   +   +   +---+   +---+   +
    |   |           |           |           |
    +   +   +---+---+   +   +---+   +---+   +
    |       |           |               |   |
    +   +---+---+   +   +---+   +---+   +---+
    |               |   |           |   |   |
    +---+   +---+---+   +---+---+   +   +   +
    |               |   |           |       |
    +---+---+---+---+---+---+---+---+---+---+

## License

Copyright © 2015 Adam Lloyd

Distributed under a BSD-style license. See [LICENSE](LICENSE).
