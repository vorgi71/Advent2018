package org.vorgi.advent2018

class Day1 {

  static void main(String[] args) {
    println "starting Day1: ${System.getProperty("user.dir")}"
    List<String> input1 = new File("data/day1_1.txt").readLines()
    def result1 = process(input1)
    println "result1 = $result1"
    def result2 = findDouble(input1)
    println "result2 = ${result2}"
  }

  static def process(List<String> input) {
    println input
    input.sum {
      it as int
    }
  }

  static def findDouble(List<String> lines) {
    def frequency=0
    def frequencies=[frequency]
    while(true) {
      for (line in lines) {
        def value = line as int
        frequency += value
        if (frequencies.contains(frequency)) {
          return frequency
        }
        frequencies << frequency
      }
    }
  }
}