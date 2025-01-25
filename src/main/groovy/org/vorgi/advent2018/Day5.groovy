package org.vorgi.advent2018

class Day5 {
  static void main(String[] args) {
    new Day5().start()
  }

  void start() {
    def input1 = "dabAcCaCBAcCcaDA"
    def result1 = reduceString(input1)
    println "result1 = $result1"
    
    def input2 = new File("data/day5_1.txt").text
    def result2=reduceString(input2)
    println "result2 = ${result2.size()}"

    def result3 = optimizeReduction(input1)
    println "result3 = $result3"
    
    def result4=optimizeReduction(input2)
    println "result4 = $result4"
  }

  String optimizeReduction(String input) {
    Set<Character> set = input.chars.collect { it.toUpperCase() }.toSet()

    Map<Character,Integer> sizeMap=[:]

    for(Character character in set) {
      char lowChar=character.toLowerCase()
      char upperChar=character.toUpperCase()
      String regex="[$lowChar|$upperChar]"
      String reducedString=input.replaceAll(regex,"")
      String result=reduceString(reducedString)
      println("$lowChar$upperChar: <$reducedString> result=$result")
      sizeMap.put(character,result.size())
    }

    sizeMap.min { it.value }.value
  }

  String reduceString(String input) {
    String output = input

    boolean stringChanged
    do {
      String newString = output
      char[] chars = output.chars
      List<String> reactions = []
      for (int index in 0..<chars.size() - 1) {
        char c = chars[index]
        char nextChar = chars[index + 1]
        if (c.toUpperCase() == nextChar.toUpperCase() &&
            (c.isLowerCase() && nextChar.isUpperCase() || c.isUpperCase() && nextChar.isLowerCase())) {
          reactions << "$c$nextChar"
          index++
        }
      }

      for(reaction in reactions) {
        newString=newString.replace(reaction,"")
      }

      stringChanged = false
      if (newString != output) {
        stringChanged = true
      }
      output = newString
    } while (stringChanged)

    output
  }
}
