package org.vorgi.advent2018

import java.awt.Point
import java.awt.Rectangle

class Day3 {
  static void main(String[] args) {
    new Day3().start()
  }

  void start() {
    def input1=new File("data/day3_1.txt").readLines()
    def result1=findOverlaps(input1)
    println "result1 = $result1"

    def input2=new File("data/day3_2.txt").readLines()
    def result2=findOverlaps(input2)
    println "result2 = $result2"
    
    def result3 = findSingle(input1)
    println "result3 = $result3"
    
    def result4 = findSingle(input2)
    println "result4 = $result4"
  }

  List<Integer> findSingle(List<String> lines) {
    Map<Integer, Rectangle> patches=parsePatches(lines)
    List<Integer> freePatches=[]
    
    for(int x in patches.keySet()) {
      Rectangle rect = patches[x]
      boolean hit=false
      for (int y in patches.keySet()) {
        if(x!=y) {
          Rectangle otherRect = patches[y]
          if (rect.intersects(otherRect)) {
            hit = true
          }
        }
      }
      if(!hit) {
        freePatches << x
      }
    }
    return freePatches
  }
  
  def findOverlaps(List<String> lines) {
    Map<Integer, Rectangle> patches=parsePatches(lines)

    Set<Rectangle> intersections=[] as Set

    for(int x in 0..<patches.size()) {
      Rectangle rect=patches.values()[x]
      for(int y in x<..<patches.size()) {
        Rectangle otherRect=patches.values()[y]
          if(rect.intersects(otherRect)) {
            intersections << rect.intersection(otherRect)
          }

      }
    }

    Set<Point> points=[] as Set

    intersections.each { Rectangle intersection ->
      for(int x in 0..<intersection.width) {
        for(int y in 0..<intersection.height) {
          points << new Point(x+intersection.minX as int, y+intersection.minY as int)
        }
      }
    }
    points.size()
  }

  Map<Integer,Rectangle> parsePatches(List<String> lines) {
    Map<Integer,Rectangle> patches=[:]

    for(line in lines) {
      int id=line[1..line.indexOf(" ",1)] as int
      String posText = line[line.indexOf("@ ")+2..<line.indexOf(":")]
      String sizeText = line[line.indexOf(": ")+2..<line.size()]

      def (int x,int y)=posText.split(",")*.toInteger()
      def (int width, int height)=sizeText.split("x")*.toInteger()
      patches.put(id,new Rectangle(x,y,width,height))
    }

    patches
  }
}
