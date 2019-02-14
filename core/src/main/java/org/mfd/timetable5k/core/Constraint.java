package org.mfd.timetable5k.core;

import org.mfd.timetable5k.core.tablerimplementations.backtracking.TimeTablerBacktrackImpl;

import java.util.List;
import java.util.Map;

@FunctionalInterface
/**
 A constraint is used to check wether a (subject, teacher ) combination can be allocated to a period. It also has methods to update its state.
 The results of the satisfy method may or may not depend on the state of the constraint.
 */
public interface Constraint {
	/**
	 Verifies whether the given subject s and teacher t meet the constriants to be alloted a period denoted by slot i,j for the classroom
	 denoted by j and the period denoted by i whose context is provided by timetable. If a constraint is not applicable in a particular
	 context then the the callback should be benign and return true.
	 Rules for creating constraints -limit the scope of constraints to only one particular entity i.e classroom or subject or teacher -a
	 single constraint should never verify the constraints of multiple entities. -use the constraint list of the entity( currently only
	 implemented for {@link Subject}) to target a particular constraint

	 @param i         the period to be alloted
	 @param j         the classroom to which the period will be alloted
	 @param timeTable the context for allocation
	 @param s         the subject to be alloted
	 @param t         the teacher to be alloted who will teach the subject

	 @return true if the teacher and subject meet constraints to be allotted to the slot denoted by i,j or if its not applicable for a given
	 context , else false
	 */
	boolean satisfies(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t);


	/**
	 Should be called before beginning timetabling. Used for pre-initialization of states of the constraints based on the periods alloted to
	 the teachers by the previous timetabler.
	 */
	default void initialize(Classroom[] classrooms,
			int nPeriodADay,
			int nDays,
			Map<Teacher, Map<Integer, Teacher.AllotedPeriod>> teacherAllotedMap){}



	/**
	 Should be called after or before allocating a (subject, teacher) to a timetable
	 */
	default void allocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
	}

	/**
	 Should be called before or after deallocation of a (subject, teacher) to a timetable
	 */
	default void deallocate(int i, int j, TimeTablerBacktrackImpl timeTable, Subject s, List<Teacher> t) {
	}

}
