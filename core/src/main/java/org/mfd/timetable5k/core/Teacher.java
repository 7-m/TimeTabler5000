package org.mfd.timetable5k.core;

import java.io.Serializable;
import java.util.*;

public class Teacher
		implements Serializable {
	private static final long serialVersionUID = 8946303608205207457L;


	private String name;
	private int    maxWorkUnits;
	private int    currentWorkUnits;

	/* periods the teacher cannot take */
	private List<Integer> restrictions;

	/* maintain a list of allocated periods */
	Map<Integer, AllotedPeriod> allocatedPeriodsMap = new HashMap<>();

	public Teacher(String name, int maxWorkUnits, List<Integer> restrictions) {
		this.name = name.toLowerCase().trim();
		this.maxWorkUnits = maxWorkUnits;
		this.restrictions = new ArrayList<>(restrictions);

	}


	public void addAllocatedPeriod(int periodNo, String subjectName, String classRoomName){
		if (allocatedPeriodsMap.containsKey(periodNo))
			throw new RuntimeException("period should be removed before allocating a new  one");
		allocatedPeriodsMap.put(periodNo, new AllotedPeriod(classRoomName,subjectName));
	}

	public Map<Integer, AllotedPeriod> getAllocatedPeriodsMap() {
		return Collections.unmodifiableMap(allocatedPeriodsMap);
	}

	public void removeAllocatedPeriod(int periodNo){
		if (!allocatedPeriodsMap.containsKey(periodNo))
			throw new RuntimeException("only allocated periods can be removed");
		allocatedPeriodsMap.remove(periodNo);
	}

	/**
	 Return AllocatedPeriod if present else null.
	 @param periodNo
	 @return
	 */
	public AllotedPeriod hasPeriod(int periodNo){
		return allocatedPeriodsMap.get(periodNo);
	}

	public List<Integer> getRestrictions() {
		return restrictions;
	}

	/*
	 * keeping a track of the current work units is not required as when assiging
	 * the classes to the teacher it w
	 */

	public int getMaxWorkUnits() {
		return maxWorkUnits;
	}

	public boolean canTake(int period) {
		return !restrictions.contains(period);
	}

	void incWorkBy(int units) {
		if (currentWorkUnits + units > maxWorkUnits)
			throw new IllegalStateException("Already saturated with work");
		currentWorkUnits += units;

	}

	void decWorkBy(int units) {
		if (currentWorkUnits - units < 0)
			throw new IllegalStateException("Work understaturated");
		currentWorkUnits -= units;
	}

	public boolean canDoWorkFor(int units) {
		return currentWorkUnits + units <= maxWorkUnits;
	}

	public String getName() {
		return name;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj instanceof Teacher)
			return this.name.equals(((Teacher) obj).getName());
		return false;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[name: " + name + ", maxWorkUnits: " + maxWorkUnits + ", currentWorkUnits: " + currentWorkUnits + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	void setAllocatedPeriodsMap(Map<Integer, AllotedPeriod> allocatedPeriodsMap) {
		this.allocatedPeriodsMap = allocatedPeriodsMap;
	}

	public static class AllotedPeriod implements Serializable {
		String classroomName;
		String SubjectName;

		private AllotedPeriod( String classroomName, String subjectName) {
			this.classroomName = classroomName;
			SubjectName = subjectName;
		}

		public String getClassroomName() {
			return classroomName;
		}

		public String getSubjectName() {
			return SubjectName;
		}
	}
}
