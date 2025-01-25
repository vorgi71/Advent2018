package org.vorgi.advent2018

import groovy.transform.ToString
import groovy.transform.TupleConstructor

class Day7 {
  static void main(String[] args) {
    new Day7().start()
  }

  void start() {
    def input1 = new File("data/day7_1.txt").readLines()
    def result1 = calculateDependency(input1)
    println "result1 = $result1"
    
    def input2 = new File("data/day7_2.txt").readLines()
    def result2 = calculateDependency(input2)
    println "result2 = $result2"
  }

  @ToString
  static class Step {
    String name
    List<String> dependsOn = []
  }

  String calculateDependency(List<String> input) {
    Map<String, Step> stepMap = [:]

    for (line in input) {
      String dependsOn = line[5..<6]
      String stepName = line[36..<37]
      Step step = stepMap.computeIfAbsent(stepName) { new Step(name: it) }
      step.dependsOn << dependsOn
      if (!stepMap.containsKey(dependsOn)) {
        stepMap.put(dependsOn, new Step(name: dependsOn))
      }
    }

    stepMap.values().forEach { step ->
      println(step)
    }

    List<String> order = []

    int stepMapSize = stepMap.size()

    while (order.size() < stepMapSize) {
      Map<String, Step> independentSteps = stepMap.findAll { name, step ->
        step.dependsOn.every { order.contains(it) }
      }
      println(independentSteps)
      String nextStep = independentSteps.keySet().sort().first
      println "processing $nextStep"
      order << nextStep
      stepMap.remove(nextStep)

    }

    return order.join("")
  }
}
