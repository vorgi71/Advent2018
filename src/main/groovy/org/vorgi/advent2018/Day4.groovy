package org.vorgi.advent2018

import groovy.transform.ToString
import groovy.transform.TupleConstructor

class Day4 {
  static void main(String[] args) {
    new Day4().start()
  }

  void start() {
    def input1=new File("data/day4_1.txt").readLines()
    def result1=findSleepingGuard(input1)
    println "result1 = $result1"

    def input2=new File("data/day4_2.txt").readLines()
    def result2=findSleepingGuard(input2)
    println "result2 = $result2"
    
    def result3=findMostSleepMinute(input1)
    println "result3 = $result3"
    
    def result4 = findMostSleepMinute(input2)
    println "result4 = $result4"
  }

  long parseTime(String text) {
    long day=text[4..<8].toLong()
    long hour=text[8..<10].toLong()
    long minute=text[10..<12].toLong()

    (day*24+hour)*60+minute

  }

  int[] mapSchedule(List<GuardState> guardStates) {

    long startTime=-1
    boolean lastStateIsActive=true

    int[] sleepArray=new int[60]

    for (GuardState state in guardStates) {
      if(state.shiftStart) {
        startTime=0
        lastStateIsActive=true
        continue
      }
      if(state.active) {
        if(lastStateIsActive==false) {
          for (int index in startTime..<state.timeStamp % 60) {
            sleepArray[index] += 1
          }
        }
        lastStateIsActive=true
        startTime=state.timeStamp%60
      }
      if(!state.active) {
        lastStateIsActive=false
        startTime=state.timeStamp%60
      }
    }
    sleepArray
  }

  int findMostSleepMinute(List<String> lines) {
    Map<Integer,List<GuardState>> guardSchedule=parseGuardState(lines)
    
    Map<Integer,Tuple2<Integer,Integer>> maxMap=[:]
    
    for(int guardID in guardSchedule.keySet()) {
      int[] sleepArray = mapSchedule(guardSchedule.get(guardID))
      int max=sleepArray.max()
      int maxIndex = sleepArray.findIndexOf { it== max}
      maxMap[guardID] = new Tuple2<>(max,maxIndex)
    }

    Map.Entry<Integer, Tuple2<Integer,Integer>> maxEntry = maxMap.max { it.value.v1 }
    maxEntry.key*maxEntry.value.v2
  }

  @TupleConstructor
  static class GuardState {
    long timeStamp
    boolean active
    boolean shiftStart

    @Override
    String toString() {
      "timeStamp: $timeStamp active: $active"
    }
  }

  def findSleepingGuard(List<String> lines) {
    Map<Integer,List<GuardState>> guardSchedule=parseGuardState(lines)

    Map<Integer,Long> sleepTimes=[:]

    for(int guardID in guardSchedule.keySet()) {
      long sleepTime=0L
      long asleepTimeStamp=0L
      for(GuardState state in guardSchedule[guardID]) {
        if(!state.active) {
          asleepTimeStamp=state.timeStamp
        } else {
          if(asleepTimeStamp!=0) {
            sleepTime+=state.timeStamp-asleepTimeStamp
          }
          asleepTimeStamp=0L
        }
      }
      sleepTimes[guardID]=sleepTime
    }
    def sleeperGuard = sleepTimes.max { it.value }

    int[] sleepArray = mapSchedule(guardSchedule.get(sleeperGuard.key))

    println("sleepArray: $sleepArray")
    
    int max=sleepArray.max()
    
    int maxIndex=sleepArray.findIndexOf { it == max }

    maxIndex*sleeperGuard.key
  }

  Map<Integer,List<GuardState>> parseGuardState(List<String> lines) {
    int currentGuard=-1

    lines.sort { it[1..16].replaceAll(/[:\- ]/,"") }

    Map<Integer,List<GuardState>> guardSchedule=[:]

    for(line in lines) {
      long eventTime=parseTime(line[1..16].replaceAll(/[:\- ]/,""))
      String restLine=line[19..-1]
      if(restLine.startsWith("Guard")) {
        currentGuard=restLine[restLine.indexOf("#")+1..<restLine.indexOf("be")].toInteger()
        guardSchedule.computeIfAbsent(currentGuard) { [] }.add(new GuardState(eventTime,true,true))
      }
      if(restLine.startsWith("wakes")) {
        guardSchedule.get(currentGuard).add(new GuardState(eventTime,true,false))
      }
      if(restLine.startsWith("falls")) {
        guardSchedule[currentGuard].add(new GuardState(eventTime,false,false))
      }
    }

    for(List<GuardState> eventList in guardSchedule.values()) {
      eventList.sort { it.timeStamp}
    }

    guardSchedule
  }
}
