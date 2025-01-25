package org.vorgi.advent2018

import groovy.transform.TupleConstructor

class Day7 {
  static void main(String[] args) {
    new Day7().start()
  }

  void start() {
    def input1=new File("data/day7_1.txt").readLines()
    def result1=calculateDependency(input1)
    println "result1 = $result1"
  }

  @TupleConstructor
  static class Step {
    String name
    List<String> dependsOn
  }

  String calculateDependency(List<String> input) {
    Map<String,Step> stepMap=[:]


  }
}
