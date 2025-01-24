package org.vorgi.advent2018

class Day2 {
  static void main(String[] args) {
    new Day2().start()
  }

  void start() {
    def input1 = new File("data/day2_1.txt").readLines()
    def result1 = countDoubleAndTriple(input1)
    println "result1 = $result1"

    def input2 = new File("data/day2_2.txt").readLines()
    def result2=countDoubleAndTriple(input2)
    println "result2 ${result2.v1*result2.v2}"
    
    def result3=findAlmostSimilar(input1)
    println "result3 = $result3"
    
    def result4=findAlmostSimilar(input2)
    println "result4 = $result4"
  }

  Tuple2 countDoubleAndTriple(List<String> strings) {
    int triples = 0
    int doubles = 0

    for (line in strings) {
      line = line.chars.collect().sort().join("")+"?"
      println("line $line")

      String currentChar = line[0]
      int count = 1
      Map<Integer, List<String>> counters = [:]
      for (c in line[1..-1]) {
        if (c == currentChar) {
          count++
        } else {
          List<String> countList = counters.computeIfAbsent(count) { [] }
          countList.add(currentChar)
          currentChar = c
          count = 1
        }
      }
      println(counters)
      if(counters.get(2)) doubles++
      if(counters.get(3)) triples++
    }
    return new Tuple2(doubles, triples)
  }

  int countDifferences(String string,String otherString) {
    int difference=0
    for(int index in 0..<string.size()) {
      if(string[index]!=otherString[index]) {
        difference++
      }
    }
    
    difference
  }

  String similarChars(String s1,String s2) {
    String outString=""
    for(int index in 0..<s1.size()) {
      if(s1[index]==s2[index]) {
        outString+=s1[index]
      }
    }

    outString
  }

  String findAlmostSimilar(List<String> input) {
    for(int index in 0..<input.size()) {
      for(int otherIndex in 0..<input.size()) {
        if(countDifferences(input[index],input[otherIndex])==1) {
          return similarChars(input[index],input[otherIndex])
        }
      }
    }
    return null
  }
}
