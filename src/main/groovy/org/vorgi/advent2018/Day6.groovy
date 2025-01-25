package org.vorgi.advent2018

import groovy.transform.CompileStatic

import java.awt.Point
import java.awt.Rectangle

import static java.lang.Math.abs

class Day6 {
  static void main(String[] args) {
    new Day6().start()
  }

  void start() {
    def input1 = new File("data/day6_1.txt").readLines()
    def result1 = findLargestFinite(input1)
    println "result1 = $result1"

    def input2 = new File("data/day6_2.txt").readLines()
    def result2 = findLargestFinite(input2)
    println "result2 = $result2"

    def result3 = calcMinDistance(input1, 32)
    println "result3 = $result3"

    def result4 = calcMinDistance(input2, 10_000,2_500)
    println "result4 = $result4"
  }

  int findLargestFinite(List<String> input) {
    List<Point> points = []
    for (line in input) {
      List<Integer> coordinates = line.split(",")*.trim()*.toInteger()
      points.add(new Point(coordinates[0], coordinates[1]))
    }

    int[][] grid = createGrid(points, 10)

    Set<Integer> infinites = [] as Set

    int width = grid[0].length
    int height = grid.length

    for (int x in 0..<width) {
      infinites.add(grid[0][x])
      infinites.add(grid[height - 1][x])
    }
    for (int y in 0..<height) {
      infinites.add(grid[y][0])
      infinites.add(grid[y][width - 1])
    }

    Map<Integer, Integer> countMap = [:]

    for (int y in 0..<height) {
      for (int x in 0..<width) {
        int value = grid[y][x]
        if (!infinites.contains(value)) {
          int count = countMap.getOrDefault(value, 0)
          countMap.put(value, count + 1)
        }
      }
    }

    println(countMap)

    return countMap.max { it.value }.value

  }

  private int[][] createGrid(ArrayList<Point> points, int offset) {
    int minX = points.min { it.@x }.@x - offset
    int maxX = points.max { it.@x }.@x + offset
    int minY = points.min { it.@y }.@y - offset
    int maxY = points.max { it.@y }.@y + offset

    Rectangle area = new Rectangle(minX, minY, maxX - minX, maxY - minY)

    points*.translate(-minX, -minY)

    int[][] grid = new int[area.height][area.width]


    for (y in 0..<grid.length) {
      for (x in 0..<grid[y].length) {
        List<Integer> distances = points.collect { abs(it.@x - x) + abs(it.@y - y) }
        int min = distances.min { it }

        def foundMin = distances.findAll { it == min }

        if (foundMin.size() == 1) {
          grid[y][x] = distances.indexOf(min)
        } else {
          grid[y][x] = -1
        }
      }
    }

    grid
  }

  void printGrid(int[][] grid) {
    for (y in 0..<grid.length) {
      for (x in grid[y]) {
        if (x == -1) {
          print(".")
        } else {
          print(x)
        }
      }
      println()
    }
  }

  def calcMinDistance(List<String> input, int maxDist, offset = 5) {
    List<Point> points = []
    for (line in input) {
      List<Integer> coordinates = line.split(",")*.trim()*.toInteger()
      points.add(new Point(coordinates[0], coordinates[1]))
    }

    int minX = points.min { it.@x }.@x - offset
    int maxX = points.max { it.@x }.@x + offset
    int minY = points.min { it.@y }.@y - offset
    int maxY = points.max { it.@y }.@y + offset

    List<Point> pointList = []

    for (int y in minX..maxX) {
      for (int x in minY..maxY) {
        if (calculateDist(x, y, points) < maxDist) {
          pointList << new Point(x, y)
        }
      }
      println("$y")
    }
    return pointList.size()
  }

  @CompileStatic
  long calculateDist(int x, int y, ArrayList<Point> points) {
    long sum=0L
    for(Point point in points) {
      sum+=abs(point.@x - x) + abs(point.@y - y)
    }
    sum
  }
}
