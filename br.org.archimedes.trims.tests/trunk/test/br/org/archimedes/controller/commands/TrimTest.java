/*
 * Created on 15/05/2006
 */

package br.org.archimedes.controller.commands;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.org.archimedes.Tester;
import br.org.archimedes.Utils;
import br.org.archimedes.arc.Arc;
import br.org.archimedes.circle.Circle;
import br.org.archimedes.controller.Controller;
import br.org.archimedes.exceptions.InvalidArgumentException;
import br.org.archimedes.exceptions.NullArgumentException;
import br.org.archimedes.infiniteline.InfiniteLine;
import br.org.archimedes.line.Line;
import br.org.archimedes.model.Drawing;
import br.org.archimedes.model.Element;
import br.org.archimedes.model.Point;
import br.org.archimedes.model.Rectangle;
import br.org.archimedes.polyline.Polyline;
import br.org.archimedes.trims.TrimCommand;

public class TrimTest extends Tester {

    private Controller ctrlInstance;

    private Drawing drawing;


    @Before
    public void setUp () {

        ctrlInstance = Controller.getInstance();
        drawing = new Drawing("test");
        ctrlInstance.setActiveDrawing(drawing);
        ctrlInstance.deselectAll();

    }

    @After
    public void tearDown () throws Exception {

        ctrlInstance = null;
        drawing = null;
    }

    private void doTrim (Element element, Collection<Element> references,
            Point click) {

        for (Element reference : references) {
            putSafeElementOnDrawing(reference, drawing);
        }

        if ( !references.contains(element)) {
            putSafeElementOnDrawing(element, drawing);
        }

        List<Point> clickPoints = new LinkedList<Point>();
        clickPoints.add(click);
        TrimCommand trimCommand = new TrimCommand(references, clickPoints);
        try {
            trimCommand.doIt(drawing);
        }
        catch (Exception e) {
            Assert.fail("Should not throw any exception");
        }
    }

    @Test
    public void testTrimHorizontalLine () throws InvalidArgumentException {

        Element line;
        Element reference1, reference2;
        Element resultLine1, resultLine2;
        Collection<Element> references;
        Point click;

        references = new LinkedList<Element>();
        line = new Line(0, 0, 50, 0);
        reference1 = new Line(10, -10, 10, 10);
        references.add(reference1);
        reference2 = new Line(20, -10, 20, 10);
        references.add(reference2);

        click = new Point(5, 0);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(10, 0, 50, 0);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(15, 0);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 0, 10, 0);
        resultLine2 = new Line(20, 0, 50, 0);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine2);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(30, 0);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 0, 20, 0);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);
    }

    @Test
    public void testTrimVerticalLine () throws InvalidArgumentException {

        Element line;
        Element reference1, reference2;
        Element resultLine1, resultLine2;
        Collection<Element> references;
        Point click;

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        references = new LinkedList<Element>();
        line = new Line(0, 0, 0, 500);
        reference1 = new Line( -100, 100, 100, 100);
        references.add(reference1);
        reference2 = new Line( -100, 200, 100, 200);
        references.add(reference2);

        click = new Point(0, 0);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 100, 0, 500);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(0, 150);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 0, 0, 100);
        resultLine2 = new Line(0, 200, 0, 500);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine2);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(0, 300);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 0, 0, 200);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);
    }

    @Test
    public void testTrimObliqueLine () throws InvalidArgumentException {

        Element line;
        Element reference1, reference2;
        Element resultLine1, resultLine2;
        Collection<Element> references;
        Point click;

        references = new LinkedList<Element>();
        line = new Line(0, 0, 500, 500);
        reference1 = new Line(0, 100, 100, 0);
        references.add(reference1);
        reference2 = new Line(0, 200, 200, 0);
        references.add(reference2);

        click = new Point(30, 30);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(50, 50, 500, 500);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);
        click = new Point(70, 70);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 0, 50, 50);
        resultLine2 = new Line(100, 100, 500, 500);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine2);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);
        click = new Point(300, 300);
        doTrim(line, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultLine1 = new Line(0, 0, 100, 100);
        assertCollectionContains(drawing.getUnlockedContents(), resultLine1);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);
    }

    // TODO Extract to specific trim handlers

    // @Test
    // public void testTrimHorizontalSemiLine () throws InvalidArgumentException
    // {
    //
    // Element semiLine;
    // Element reference1, reference2;
    // Element resultElement1, resultElement2;
    // Collection<Element> references;
    // Point click;
    //
    // references = new ArrayList<Element>();
    // semiLine = new SemiLine(0, 0, 500, 0);
    // reference1 = new Line(100, -100, 100, 100);
    // references.add(reference1);
    // reference2 = new Line(200, -100, 200, 100);
    // references.add(reference2);
    //
    // click = new Point(50, 0);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(100, 0, 500, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(150, 0);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only four elements.", 4,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new Line(0, 0, 100, 0);
    // resultElement2 = new SemiLine(200, 0, 500, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(300, 0);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new Line(0, 0, 200, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // }
    //
    // @Test
    // public void testTrimVerticalSemiLine () throws InvalidArgumentException {
    //
    // Element semiLine;
    // Element reference1, reference2;
    // Element resultElement1, resultElement2;
    // Collection<Element> references;
    // Point click;
    //
    // references = new ArrayList<Element>();
    // semiLine = new SemiLine(0, 0, 0, 500);
    // reference1 = new Line( -100, 100, 100, 100);
    // references.add(reference1);
    // reference2 = new Line( -100, 200, 100, 200);
    // references.add(reference2);
    //
    // click = new Point(0, 50);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(0, 100, 0, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(0, 150);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only four elements.", 4,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new Line(0, 0, 0, 100);
    // resultElement2 = new SemiLine(0, 200, 0, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(0, 300);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new Line(0, 0, 0, 200);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    // }
    //
    // @Test
    // public void testTrimObliqueSemiLine () throws InvalidArgumentException {
    //
    // Element semiLine;
    // Element reference1, reference2;
    // Element resultElement1, resultElement2;
    // Collection<Element> references;
    // Point click;
    //
    // references = new ArrayList<Element>();
    // semiLine = new SemiLine(0, 0, 100, 100);
    // reference1 = new Line(0, 100, 100, 0);
    // references.add(reference1);
    // reference2 = new Line(0, 200, 200, 0);
    // references.add(reference2);
    //
    // click = new Point(30, 30);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(50, 50, 500, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(70, 70);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only four elements.", 4,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new Line(0, 0, 50, 50);
    // resultElement2 = new SemiLine(100, 100, 500, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(300, 300);
    // doTrim(semiLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new Line(0, 0, 100, 100);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    // }
    //
    // @Test
    // public void testTrimHorizontalInfiniteLine ()
    // throws InvalidArgumentException {
    //
    // Element infiniteLine;
    // Element reference1, reference2;
    // Element resultElement1, resultElement2;
    // Collection<Element> references;
    // Point click;
    //
    // references = new ArrayList<Element>();
    // infiniteLine = new InfiniteLine(0, 0, 500, 0);
    // reference1 = new Line(100, -100, 100, 100);
    // references.add(reference1);
    // reference2 = new Line(200, -100, 200, 100);
    // references.add(reference2);
    //
    // click = new Point(50, 0);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(100, 0, 500, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(150, 0);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only four elements.", 4,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(100, 0, 0, 0);
    // resultElement2 = new SemiLine(200, 0, 500, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(300, 0);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(200, 0, 0, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // }
    //
    // @Test
    // public void testTrimVerticalInfiniteLine () throws
    // InvalidArgumentException {
    //
    // Element infiniteLine;
    // Element reference1, reference2;
    // Element resultElement1, resultElement2;
    // Collection<Element> references;
    // Point click;
    //
    // references = new ArrayList<Element>();
    // infiniteLine = new InfiniteLine(0, 0, 0, 500);
    // reference1 = new Line( -100, 100, 100, 100);
    // references.add(reference1);
    // reference2 = new Line( -100, 200, 100, 200);
    // references.add(reference2);
    //
    // click = new Point(0, 50);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(0, 100, 0, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(0, 150);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only four elements.", 4,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(0, 100, 0, 0);
    // resultElement2 = new SemiLine(0, 200, 0, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    // click = new Point(0, 300);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(0, 200, 0, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    // }
    //
    // @Test
    // public void testTrimObliqueInfiniteLine () throws
    // InvalidArgumentException {
    //
    // Element infiniteLine;
    // Element reference1, reference2;
    // Element resultElement1, resultElement2;
    // Collection<Element> references;
    // Point click;
    //
    // references = new ArrayList<Element>();
    // infiniteLine = new InfiniteLine(0, 0, 100, 100);
    // reference1 = new Line(0, 100, 100, 0);
    // references.add(reference1);
    // reference2 = new Line(0, 200, 200, 0);
    // references.add(reference2);
    //
    // click = new Point(30, 30);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(50, 50, 500, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    //
    // click = new Point(70, 70);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only four elements.", 4,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(50, 50, 0, 0);
    // resultElement2 = new SemiLine(100, 100, 500, 500);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    //
    // drawing = new Drawing("");
    // ctrlInstance.setActiveDrawing(drawing);
    //
    // click = new Point(300, 300);
    // doTrim(infiniteLine, references, click);
    //
    // Assert.assertEquals("Drawing should contains only three elements.", 3,
    // drawing.getUnlockedContents().size());
    // resultElement1 = new SemiLine(100, 100, 0, 0);
    // assertCollectionContains(drawing.getUnlockedContents(), resultElement1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference1);
    // assertCollectionContains(drawing.getUnlockedContents(), reference2);
    // }

    @Test
    public void testTrimArc () throws Exception {

        Point init = new Point( -100, 0);
        Point mid = new Point(0, 100);
        Point end = new Point(100, 0);

        Element arc = new Arc(init, mid, end);

        Element reference1, reference2;
        Element resultElement1, resultElement2;
        Collection<Element> references;
        Point click;

        references = new LinkedList<Element>();
        reference1 = new InfiniteLine( -COS_45 * 100, 0, -COS_45 * 100, 100);
        references.add(reference1);
        reference2 = new InfiniteLine(COS_45 * 100, 0, COS_45 * 100, 100);
        references.add(reference2);

        click = new Point(0, 100);
        doTrim(arc, references, click);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());

        resultElement1 = new Arc(init, new Point( -COS_30 * 100, COS_60 * 100),
                new Point( -COS_45 * 100, COS_45 * 100));
        assertCollectionContains(drawing.getUnlockedContents(), resultElement1);

        resultElement2 = new Arc(new Point(COS_45 * 100, COS_45 * 100),
                new Point(COS_30 * 100, COS_60 * 100), end);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        arc = new Arc(init, mid, end);
        references = new LinkedList<Element>();
        reference1 = new InfiniteLine(0, 0, 0, 100);
        references.add(reference1);

        click = new Point( -100, 0);

        doTrim(arc, references, click);

        Assert.assertEquals("Drawing should contains only two elements.", 2,
                drawing.getUnlockedContents().size());

        resultElement1 = new Arc(mid, new Point(COS_45 * 100, COS_45 * 100),
                end);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement1);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        arc = new Arc(init, mid, end);
        references = new LinkedList<Element>();
        reference1 = new InfiniteLine(0, 0, 0, 100);
        references.add(reference1);

        click = new Point(100, 0);

        doTrim(arc, references, click);

        Assert.assertEquals("Drawing should contains only two elements.", 2,
                drawing.getUnlockedContents().size());

        resultElement1 = new Arc(init, new Point( -COS_45 * 100, COS_45 * 100),
                mid);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement1);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        init = new Point(0, -100);
        mid = new Point(100, 0);
        end = new Point(0, 100);

        arc = new Arc(init, mid, end);

        references = new LinkedList<Element>();
        reference1 = new InfiniteLine(0, COS_45 * 100, 100, COS_45 * 100);
        references.add(reference1);
        reference2 = new InfiniteLine(0, -COS_45 * 100, 100, -COS_45 * 100);
        references.add(reference2);

        click = new Point(100, 0);

        doTrim(arc, references, click);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());

        resultElement1 = new Arc(init, new Point(COS_60 * 100, -COS_30 * 100),
                new Point(COS_45 * 100, -COS_45 * 100));
        assertCollectionContains(drawing.getUnlockedContents(), resultElement1);

        resultElement2 = new Arc(new Point(COS_45 * 100, COS_45 * 100),
                new Point(COS_60 * 100, COS_30 * 100), end);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement2);
    }

    @Test
    public void testTrimPolylineIsALine () throws Exception {

        List<Point> points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 100));
        Polyline polyline = new Polyline(points);

        Collection<Element> references = new LinkedList<Element>();
        Element reference1 = new Line( -50, 20, 50, 20);
        references.add(reference1);
        Element reference2 = new Line( -50, 40, 50, 40);
        references.add(reference2);
        Element reference3 = new Line( -50, 60, 50, 60);
        references.add(reference3);

        Point click = new Point(0, 100);
        doTrim(polyline, references, click);
        polyline = (Polyline) getElementAt(0, 0);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 60));
        Element resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(0, 30);
        doTrim(polyline, references, click);

        Assert.assertEquals("Drawing should contains only five elements.", 5,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(0, 20));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        points = new LinkedList<Point>();
        points.add(new Point(0, 40));
        points.add(new Point(0, 60));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);
    }

    @Test
    public void testTrimPolyLine () throws Exception {

        List<Point> points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(50, 50));
        points.add(new Point(100, 0));
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        Polyline polyline = new Polyline(points);

        Collection<Element> references = new LinkedList<Element>();
        Element reference1 = new InfiniteLine( -100, 25, 300, 25);
        references.add(reference1);

        Point click = new Point(0, 0);
        doTrim(polyline, references, click);
        polyline = (Polyline) getElementAt(200, 0);

        Assert.assertEquals("Drawing should contains only two elements.", 2,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(25, 25));
        points.add(new Point(50, 50));
        points.add(new Point(100, 0));
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        Element resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(100, 0);
        doTrim(polyline, references, click);
        polyline = (Polyline) getElementAt(200, 0);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(25, 25));
        points.add(new Point(50, 50));
        points.add(new Point(75, 25));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        points = new LinkedList<Point>();
        points.add(new Point(125, 25));
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(200, 0);
        doTrim(polyline, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 2,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(125, 25));
        points.add(new Point(150, 50));
        points.add(new Point(175, 25));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(50, 50));
        points.add(new Point(100, 0));
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        polyline = new Polyline(points);

        references = new LinkedList<Element>();
        reference1 = new InfiniteLine(50, 0, 50, 10);
        references.add(reference1);
        Element reference2 = new InfiniteLine(150, 0, 150, 10);
        references.add(reference2);

        click = new Point(0, 0);
        doTrim(polyline, references, click);
        polyline = (Polyline) getElementAt(200, 0);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(50, 50));
        points.add(new Point(100, 0));
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(200, 0);
        doTrim(polyline, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(50, 50));
        points.add(new Point(100, 0));
        points.add(new Point(150, 50));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(50, 50));
        points.add(new Point(100, 0));
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        polyline = new Polyline(points);

        references = new LinkedList<Element>();
        reference1 = new InfiniteLine(50, 0, 50, 10);
        references.add(reference1);
        reference2 = new InfiniteLine(150, 0, 150, 10);
        references.add(reference2);

        click = new Point(100, 0);
        doTrim(polyline, references, click);

        Assert.assertEquals("Drawing should contains only four elements.", 4,
                drawing.getUnlockedContents().size());

        points = new LinkedList<Point>();
        points.add(new Point(0, 0));
        points.add(new Point(50, 50));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);

        points = new LinkedList<Point>();
        points.add(new Point(150, 50));
        points.add(new Point(200, 0));
        resultElement = new Polyline(points);
        assertCollectionContains(drawing.getUnlockedContents(), resultElement);
    }

    private Element getElementAt (double x, double y) {

        Rectangle rect = Utils.getSquareFromDelta(new Point(x, y), 0.25);
        Set<Element> selection;
        Element element = null;

        try {
            selection = drawing.getSelectionIntersection(rect);
            element = selection.iterator().next();
        }
        catch (NullArgumentException e) {
            // Shouldn't get to this point
        }

        return element;
    }

    @Test
    public void testTrimCircle () throws Exception {

        Element circle;
        Element reference1, reference2;
        Element resultArc;
        Collection<Element> references;
        Point click;

        references = new LinkedList<Element>();
        circle = new Circle(new Point(0, 0), 100.0);

        reference1 = new Line(0, -200, 0, 200);
        references.add(reference1);
        reference2 = new Line( -200, 0, 200, 0);
        references.add(reference2);

        click = new Point(COS_45 * 100, COS_45 * 100);
        doTrim(circle, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());
        resultArc = new Arc(new Point(0, 100), new Point( -100, 0), new Point(
                100, 0));
        assertCollectionContains(drawing.getUnlockedContents(), resultArc);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);
    }

    @Test
    public void testTrimClosedPolyLine () throws Exception {

        Element polyLine;
        Element reference1, reference2;
        Element resultPolyLine;
        Collection<Element> references;
        Point click;

        List<Point> points = new LinkedList<Point>();
        points.add(new Point( -100, -100));
        points.add(new Point( -100, 100));
        points.add(new Point(100, 100));
        points.add(new Point(100, -100));
        points.add(new Point( -100, -100));
        polyLine = new Polyline(points);

        references = new LinkedList<Element>();
        reference1 = new Line( -50, -200, -50, 200);
        references.add(reference1);
        reference2 = new Line( -200, -50, 200, -50);
        references.add(reference2);

        click = new Point( -100, -70);
        doTrim(polyLine, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());

        List<Point> resultPoints = new LinkedList<Point>();
        resultPoints.add(new Point( -100, -50));
        resultPoints.add(new Point( -100, 100));
        resultPoints.add(new Point(100, 100));
        resultPoints.add(new Point(100, -100));
        resultPoints.add(new Point( -50, -100));
        resultPolyLine = new Polyline(resultPoints);

        assertCollectionContains(drawing.getUnlockedContents(), resultPolyLine);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point( -70, -100);
        doTrim(polyLine, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());

        assertCollectionContains(drawing.getUnlockedContents(), resultPolyLine);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);

        drawing = new Drawing("");
        ctrlInstance.setActiveDrawing(drawing);

        click = new Point(100, 70);
        doTrim(polyLine, references, click);

        Assert.assertEquals("Drawing should contains only three elements.", 3,
                drawing.getUnlockedContents().size());

        resultPoints = new LinkedList<Point>();
        resultPoints.add(new Point(100, -50));
        resultPoints.add(new Point(100, -100));
        resultPoints.add(new Point( -100, -100));
        resultPoints.add(new Point( -100, 100));
        resultPoints.add(new Point( -50, 100));
        resultPolyLine = new Polyline(resultPoints);

        assertCollectionContains(drawing.getUnlockedContents(), resultPolyLine);
        assertCollectionContains(drawing.getUnlockedContents(), reference1);
        assertCollectionContains(drawing.getUnlockedContents(), reference2);
    }
}
