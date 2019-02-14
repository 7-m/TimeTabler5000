package org.mfd.timetable5k.core.tablerimplementations.backtracking;


/**
 Encapsulates the logic of selecting the next candidate subject to try to allocate a teacher to and allocating it if constraints are met.
 An interface that deallocates or attempts to allocate period(s) to the slots in the timetable while meeting constraints posed by the
 candidate subject and the teachers.

 */
public abstract class Allocator {
	private TimeTablerBacktrackImpl timeTable;

	/**
	 Callback for one time initializing of T Always call this method before extending
	 with your own code for additional initialization.

	 @param timeTable the context for all the allocation and deallocation callbacks

	 */
	protected void init(TimeTablerBacktrackImpl timeTable) {
		this.timeTable = timeTable;
	}

	protected TimeTablerBacktrackImpl getTimeTabler() {
		return timeTable;
	}

	/**
	 For allocating period to the slot expressed by i,j.
	 @param i the period index with respect to a week
	 @param j the classroom index
	 @return true if and only if a period was allocated and false if and only if there are no feasible
	 candidates for the slot.
	 */
	protected abstract boolean allocate(int i, int j);

	/**
	 Used to deallocate an already allocated period. The method will be called on an allocated slot only. This method
	 should deallocate only what its counter part method allocated
	 @param i the period index with respect to a week
	 @param j the classroom indexeTable the context for the allocation
	 */
	protected abstract void deallocate(int i, int j);

	/**
	 Used to check whether a candidate (a subject and teacher which MAY OR MAY NOT meet constraints for allocation)
	 exits for allocation.
	 @param i the period index with respect to a week
	 @param j the classroom index

	 @return true if and only if there exists a candidate for slot denoted by i,j
	 */
	protected abstract boolean hasCandidateFor(int i, int j);
}
