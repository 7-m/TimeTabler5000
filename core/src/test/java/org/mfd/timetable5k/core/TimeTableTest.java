/*
package org.mfd.timetable5k.core;


import org.junit.jupiter.api.Test;
import org.mfd.timetable5k.gui.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimeTableTest {

    @Test
    //@Disabled
    public void timeTableMethod() {

        // create a mock subbjects
        Subject s0 = new Subject("s0", 3);
        Subject s1 = new Subject("s1", 3);
        Subject s2 = new Subject("s2", 2);
        Subject s3 = new Subject("s3", 2);

        Teacher t0 = new Teacher("t0", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t1 = new Teacher("t1", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t2 = new Teacher("t2", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t3 = new Teacher("t3", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t4 = new Teacher("t4", Integer.MAX_VALUE, Collections.emptyList());

        // c0 -> s0, s1, s2
        // c1 -> s1, s2, s3
        // c2 -> s0, s1, s3

        Classroom c0 = new Classroom("c0", Arrays.asList(s0, s1, s2 ));
        Classroom c1 = new Classroom("c1", Arrays.asList(s1, s2, s3));
        Classroom c2 = new Classroom("c2", Arrays.asList(s0, s1, s3 ));

        // t0->s0, s3
        // t1->s0, s1, s2
        // t2->s1
        // t3->s2
        // t4->s3

        // is it necessary to assign the free period a teachers? yep cause teacher
        // constraints are checked
        c0.assignTeacher(s0, t0);
        c0.assignTeacher(s1, t1);
        c0.assignTeacher(s2, t1);


        c1.assignTeacher(s1, t2);
        c1.assignTeacher(s2, t3);
        c1.assignTeacher(s3, t0);

        c2.assignTeacher(s0, t1);
        c2.assignTeacher(s1, t2);
        c2.assignTeacher(s3, t4);

        // verifyTimeTable the first 100k lines, if mismatch, print the original and mismatched
        // this test is terribly written, fix this ASAP
        CheckUtils.normalize(3,3,new Classroom[]{c0, c1, c2});
        TimeTablerReferenceImpl tb = new TimeTablerReferenceImpl(3, 3, new Classroom[]{c0, c1, c2}, new int[]{},Integer.MAX_VALUE);


        for (int i = 0; i < 100_000; i++) {
            assertTrue(Utils.verifyTimeTable(tb, tb.nextTimeTable()));
        }
        tb.stopTimeTabling();
    }

    @Test
   // @Disabled
    public void timeTableWithLabsMethod() {

        // TODO: TTEST FOR LAB0 IS INVALID, YOU FORGOT LAB MODIFIER!!!!
        Subject s0 = new Subject("s0", 3);
        Subject s1 = new Subject("s1", 3);
        Subject s2 = new Subject("s2", 2);
        Subject s3 = new Subject("s3", 2);
        Subject lab0 = new Subject("lab0", 4,"lab"); //check here...

        //Subject free1 = new Subject("free", 3, false);// represents a single free period
       // Subject free2 = new Subject("free", 4, false);

        Teacher t0 = new Teacher("t0", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t1 = new Teacher("t1", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t2 = new Teacher("t2", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t3 = new Teacher("t3", Integer.MAX_VALUE, Collections.emptyList());
        Teacher t4 = new Teacher("t4", Integer.MAX_VALUE, Collections.emptyList());
        Teacher lt0 = new Teacher("lt0", 12, Collections.emptyList());
		Teacher lt1 = new Teacher("lt1", 12, Collections.emptyList());

		// c0 -> s0, s1, s2
        // c1 -> s1, s2, s3
        // c2 -> s0, s1, s3

        */
/*Classroom c0 = new Classroom("c0", Arrays.asList(s0, s1, s2, lab0, free1));
        Classroom c1 = new Classroom("c1", Arrays.asList(s1, s2, s3, lab0, free2));
        Classroom c2 = new Classroom("c2", Arrays.asList(s0, s1, s3, lab0, free1));*//*


		Classroom c0 = new Classroom("c0", new ArrayList<>(Arrays.asList(s0, s1, s2, lab0)));
		Classroom c1 = new Classroom("c1", new ArrayList<>(Arrays.asList(s1, s2, s3, lab0)));
		Classroom c2 = new Classroom("c2", new ArrayList<>(Arrays.asList(s0, s1, s3, lab0)));

        // t0->s0, s3
        // t1->s0, s1, s2
        // t2->s1
        // t3->s2
        // t4->s3

        // is it necessary to assign the free period a teachers? yep cause teacher
        // constraints are checked
        c0.assignTeacher(s0, t0);
        c0.assignTeacher(s1, t1);
        c0.assignTeacher(s2, t1);
		c0.assignTeacher(lab0, lt0);
      //  c0.assignTeacher(free1, new Teacher("c0 free", 0, Collections.emptyList()));


        c1.assignTeacher(s1, t2);
        c1.assignTeacher(s2, t3);
        c1.assignTeacher(s3, t0);
		c1.assignTeacher(lab0, lt1);// use another teacher cause no solution exists (work it out on paper)
       // c1.assignTeacher(free2, new Teacher("c1 free", 0, Collections.emptyList()));

        c2.assignTeacher(s0, t1);
        c2.assignTeacher(s1, t2);
        c2.assignTeacher(s3, t4);
		c2.assignTeacher(lab0, lt0);

		// c2.assignTeacher(free1, new Teacher("c2 free", 0, Collections.emptyList()));

		CheckUtils.normalize(3,5,new Classroom[]{c0, c1, c2});

        TimeTablerReferenceImpl
                tb = new TimeTablerReferenceImpl(3, 5, new Classroom[]{c0, c1, c2}, new int[]{2}, Integer.MAX_VALUE);




        for (int i = 0; i < 100_000; i++) {
            assertTrue(Utils.verifyTimeTable(tb, tb.nextTimeTable()));
        }
        tb.stopTimeTabling();

    }



}
*/
