package br.org.archimedes.intersector.infinitelinepolyline.tests;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import br.org.archimedes.Tester;
import br.org.archimedes.exceptions.InvalidArgumentException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.infiniteline.InfiniteLine;
import br.org.archimedes.interfaces.Intersector;
import br.org.archimedes.intersector.infinitelinepolyline.InfinitelinePolylineIntersector;
import br.org.archimedes.model.Point;
import br.org.archimedes.polyline.Polyline;

public class InfinitelinePolylineIntersectorTest extends Tester {

	InfiniteLine infiniteLine;
	Intersector intersector;

	@Before
	public void setUp() throws NullArgumentException, InvalidArgumentException {
		infiniteLine = new InfiniteLine(new Point(0.0, 0.0),
				new Point(2.0, 2.0));
		intersector = new InfinitelinePolylineIntersector();
	}

	@Test
	public void nullArgumentsShouldRaiseException()
			throws NullArgumentException, InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(0.0, 3.0));
		polyPoints.add(new Point(3.0, 3.0));
		polyPoints.add(new Point(5.0, 0.0));
		Polyline testPoly = new Polyline(polyPoints);
		try {
			intersector.getIntersections(testPoly, null);
			fail("The otherElement is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(null, testPoly);
			fail("The element is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(infiniteLine, null);
			fail("The otherElement is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(null, infiniteLine);
			fail("The element is null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}

		try {
			intersector.getIntersections(null, null);
			fail("Both elements are null and getIntersections should have thrown a NullArgumentException");
		} catch (NullArgumentException e) {
			// Passed
		}
		Assert.assertTrue("Raised all exceptions", true);
	}

	@Test
	public void polylineNotIntersectingInfinitelineReturnsNoIntersections()
			throws NullArgumentException, InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(-1.0, 1.0));
		polyPoints.add(new Point(0.0, 1.0));
		polyPoints.add(new Point(-1.0, 0.0));
		Polyline testPoly = new Polyline(polyPoints);

		assertCollectionTheSame(Collections.EMPTY_LIST, intersector
				.getIntersections(testPoly, infiniteLine));
	}

	@Test
	public void polylineIntersectsInfinitelineThreeTimesReturnsThreeIntersectionPoints()
			throws NullArgumentException, InvalidArgumentException {
		List<Point> polyPoints = new ArrayList<Point>();
		polyPoints.add(new Point(-2.0, -1.0));
		polyPoints.add(new Point(0.0, -1.0));
		polyPoints.add(new Point(0.0, 1.0));
		polyPoints.add(new Point(1.0, 1.0));
		Polyline testPoly = new Polyline(polyPoints);

		Collection<Point> expected = new ArrayList<Point>();
		expected.add(new Point(-1.0, -1.0));
		expected.add(new Point(0.0, 0.0));
		expected.add(new Point(1.0, 1.0));
		
		Collection<Point> real = intersector.getIntersections(testPoly, infiniteLine);
		assertCollectionTheSame(expected, real);
	}
}
