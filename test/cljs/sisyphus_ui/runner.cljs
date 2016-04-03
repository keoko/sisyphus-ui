(ns sisyphus-ui.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [sisyphus-ui.core-test]))

(doo-tests 'sisyphus-ui.core-test)
