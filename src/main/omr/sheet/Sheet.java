//----------------------------------------------------------------------------//
//                                                                            //
//                                 S h e e t                                  //
//                                                                            //
//----------------------------------------------------------------------------//
// <editor-fold defaultstate="collapsed" desc="hdr">                          //
//  Copyright © Hervé Bitteur 2000-2012. All rights reserved.                 //
//  This software is released under the GNU General Public License.           //
//  Goto http://kenai.com/projects/audiveris to report bugs or suggestions.   //
//----------------------------------------------------------------------------//
// </editor-fold>
package omr.sheet;

import omr.Main;

import omr.constant.Constant;
import omr.constant.ConstantSet;

import omr.glyph.BasicNest;
import omr.glyph.Glyphs;
import omr.glyph.Nest;
import omr.glyph.Shape;
import omr.glyph.SymbolsModel;
import omr.glyph.facets.Glyph;
import omr.glyph.ui.SymbolsController;
import omr.glyph.ui.SymbolsEditor;

import omr.grid.StaffManager;
import omr.grid.SystemManager;
import omr.grid.TargetBuilder;

import omr.lag.Lag;
import omr.lag.Section;
import omr.lag.Sections;

import omr.log.Logger;

import omr.score.Score;
import omr.score.ScoresManager;
import omr.score.common.PixelDimension;
import omr.score.common.PixelPoint;
import omr.score.entity.Page;
import omr.score.entity.SystemNode;

import omr.selection.LocationEvent;
import omr.selection.PixelLevelEvent;
import omr.selection.SelectionService;

import omr.sheet.picture.ImageFormatException;
import omr.sheet.picture.Picture;
import omr.sheet.picture.PictureView;
import omr.sheet.ui.BoundaryEditor;
import omr.sheet.ui.PixelBoard;
import omr.sheet.ui.SheetAssembly;
import omr.sheet.ui.SheetsController;

import omr.step.Step;
import omr.step.StepException;
import omr.step.Stepping;
import omr.step.Steps;

import omr.ui.BoardsPane;
import omr.ui.ErrorsEditor;

import java.awt.Point;
import java.awt.image.RenderedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentSkipListSet;
import omr.grid.GridBuilder;

/**
 * Class {@code Sheet} is the corner stone for Sheet processing,
 * keeping pointers to all processings related to the image, and to
 * their results.
 *
 * @author Hervé Bitteur
 */
public class Sheet
{
    //~ Static fields/initializers ---------------------------------------------

    /** Specific application parameters */
    private static final Constants constants = new Constants();

    /** Usual logger utility */
    private static final Logger logger = Logger.getLogger(Sheet.class);

    /** Events that can be published on a sheet service */
    public static final Class<?>[] allowedEvents = new Class<?>[]{
        LocationEvent.class,
        PixelLevelEvent.class
    };

    //~ Instance fields --------------------------------------------------------
    //
    /** Containing score */
    private final Score score;

    /** Corresponding page */
    private final Page page;

    /** The recording of key processing data */
    private SheetBench bench;

    /** Related assembly instance, if any */
    private SheetAssembly assembly;

    /** Related errors symbolsEditor */
    private ErrorsEditor errorsEditor;

    /** Retrieved systems (populated by SYSTEMS/SystemsBuilder) */
    private final List<SystemInfo> systems;

    //-- resettable members ----------------------------------------------------
    //
    /** The related picture */
    private Picture picture;

    /** Global scale for this sheet */
    private Scale scale;

    /** Initial skew value */
    private Skew skew;

    /** Horizontal entities */
    private Horizontals horizontals;

    /** Horizontal lag */
    private Lag hLag;

    /** Vertical lag */
    private Lag vLag;

    /** Global glyph nest */
    private Nest nest;

    /**
     * Non-lag & non-glyph related selections for this sheet
     * (SheetLocation and PixelLevel)
     */
    private final SelectionService locationService;

    // Companion processors
    //
    /** Scale */
    private ScaleBuilder scaleBuilder;

    /** Staves */
    private final StaffManager staffManager;

    /** Grid */
    private GridBuilder gridBuilder;

    /** Systems */
    private final SystemManager systemManager;

    /** Bars checker */
    private volatile BarsChecker barsChecker;

    /** A bar line extractor for this sheet */
    private volatile SystemsBuilder systemsBuilder;

    /** Specific builder dealing with glyphs */
    private volatile SymbolsController symbolsController;

    /** Related verticals model */
    private volatile VerticalsController verticalsController;

    /** Related target builder */
    private volatile TargetBuilder targetBuilder;

    /** Related symbols editor */
    private SymbolsEditor symbolsEditor;

    /** Related boundary editor */
    private BoundaryEditor boundaryEditor;

    /** The current maximum value for foreground pixels */
    private Integer maxForeground;

    /** The histogram ratio to be used on this sheet to retrieve staves */
    private Double histoRatio;

    /** The step being done on this sheet */
    private Step currentStep;

    /** All steps already done on this sheet */
    private Set<Step> doneSteps = new HashSet<>();

    /** Id of last long horizontal section */
    private int lastLongHSectionId = -1;

    /** Have systems their boundaries? */
    private boolean hasSystemBoundaries = false;

    //~ Constructors -----------------------------------------------------------
    //-------//
    // Sheet //
    //-------//
    /**
     * Create a new {@code Sheet} instance, based on a couple made of
     * an image (the original pixel input) and a page (the score entities
     * output).
     *
     * @param page  the related score page
     * @param image the already loaded image
     */
    public Sheet (Page page,
                  RenderedImage image)
            throws StepException
    {
        this.page = page;
        this.score = page.getScore();

        locationService = new SelectionService("sheet", allowedEvents);

        staffManager = new StaffManager(this);
        systemManager = new SystemManager(this);
        bench = new SheetBench(this);

        systems = systemManager.getSystems();

        // Update UI information if so needed
        if (Main.getGui() != null) {
            errorsEditor = new ErrorsEditor(this);
            Main.getGui().sheetsController.createAssembly(this);
        }

        setImage(image);

        logger.fine("Created {0}", this);
    }

    //~ Methods ----------------------------------------------------------------
    //------//
    // done //
    //------//
    /**
     * Remember that the provided step has been done on the sheet
     *
     * @param step the provided step
     */
    public final void done (Step step)
    {
        doneSteps.add(step);
    }

    //----------//
    // addError //
    //----------//
    /**
     * Register an error in the sheet ErrorsWindow
     *
     * @param container the immediate container for the error location
     * @param glyph     the related glyph if any
     * @param text      the error message
     */
    public void addError (SystemNode container,
                          Glyph glyph,
                          String text)
    {
        if (Main.getGui() != null) {
            getErrorsEditor().addError(container, glyph, text);
        }
    }

    //    //----------//
    //    // colorize //
    //    //----------//
    //    /**
    //     * Set proper colors for sections of all recognized items so far, using the
    //     * provided color
    //     *
    //     * @param lag       the lag to be colorized
    //     * @param viewIndex the provided lag view index
    //     * @param color     the color to use
    //     */
    //    public void colorize (Color color)
    //    {
    //        if (score != null) {
    //            // Colorization of all known score items
    //            score.accept(new ScoreColorizer(color));
    //        } else {
    //            // Nothing to colorize ? TODO
    //        }
    //    }
    //----------------------------------//
    // createSymbolsControllerAndEditor //
    //----------------------------------//
    public void createSymbolsControllerAndEditor ()
    {
        SymbolsModel model = new SymbolsModel(this);
        symbolsController = new SymbolsController(model);

        if (Main.getGui() != null) {
            symbolsEditor = new SymbolsEditor(this, symbolsController);
        }
    }

    //----------------------//
    // createSystemsBuilder //
    //----------------------//
    public void createSystemsBuilder ()
    {
        page.resetSystems();
        systemsBuilder = new SystemsBuilder(this);
    }

    //---------------------------//
    // createVerticalsController //
    //---------------------------//
    public void createVerticalsController ()
    {
        verticalsController = new VerticalsController(this);
    }

    //-----------------//
    // dumpSystemInfos //
    //-----------------//
    /**
     * Utility method, to dump all sheet systems
     */
    public void dumpSystemInfos ()
    {
        System.out.println("--- SystemInfos ---");

        int i = 0;

        for (SystemInfo system : getSystems()) {
            Main.dumping.dump(system, "#" + i++);
        }

        System.out.println("--- SystemInfos end ---");
    }

    //-----------------//
    // getActiveGlyphs //
    //-----------------//
    /**
     * Export the active glyphs of the vertical lag.
     *
     * @return the collection of glyphs for which at least a section is assigned
     */
    public Collection<Glyph> getActiveGlyphs ()
    {
        return nest.getActiveGlyphs();
    }

    //-------------//
    // getAssembly //
    //-------------//
    /**
     * Report the related SheetAssembly for GUI
     *
     * @return the assembly, or null otherwise
     */
    public SheetAssembly getAssembly ()
    {
        return assembly;
    }

    //----------------//
    // getBarsChecker //
    //----------------//
    /**
     * @return the barsChecker
     */
    public BarsChecker getBarsChecker ()
    {
        return barsChecker;
    }

    //----------//
    // getBench //
    //----------//
    /**
     * Report the related sheet bench
     *
     * @return the related bench
     */
    public SheetBench getBench ()
    {
        return bench;
    }

    //-------------------//
    // getBoundaryEditor //
    //-------------------//
    /**
     * @return the boundaryEditor
     */
    public BoundaryEditor getBoundaryEditor ()
    {
        return boundaryEditor;
    }

    //----------------//
    // getCurrentStep //
    //----------------//
    /**
     * Retrieve the step being processed "as we speak"
     *
     * @return the current step
     */
    public Step getCurrentStep ()
    {
        return currentStep;
    }

    //-------------------------//
    // getDefaultMaxForeground //
    //-------------------------//
    public static int getDefaultMaxForeground ()
    {
        return constants.maxForegroundGrayLevel.getValue();
    }

    //--------------//
    // getDimension //
    //--------------//
    /**
     * Report the dimension of the sheet/page
     *
     * @return the page/sheet dimension in pixels
     */
    public PixelDimension getDimension ()
    {
        return picture.getDimension();
    }

    //-----------------//
    // getErrorsEditor //
    //-----------------//
    public ErrorsEditor getErrorsEditor ()
    {
        return errorsEditor;
    }

    //-----------//
    // getHeight //
    //-----------//
    /**
     * Report the picture height in pixels
     *
     * @return the picture height
     */
    public int getHeight ()
    {
        return picture.getHeight();
    }

    //------------------//
    // getHorizontalLag //
    //------------------//
    /**
     * Report the current horizontal lag for this sheet
     *
     * @return the current horizontal lag
     */
    public Lag getHorizontalLag ()
    {
        return hLag;
    }

    //----------------//
    // getHorizontals //
    //----------------//
    /**
     * Retrieve horizontals system by system
     *
     * @return the horizontals found
     */
    public Horizontals getHorizontals ()
    {
        return horizontals;
    }

    //-------//
    // getId //
    //-------//
    public String getId ()
    {
        return page.getId();
    }

    //--------------//
    // getInterline //
    //--------------//
    /**
     * Convenient method to report the scaling information of the sheet
     *
     * @return the scale interline
     */
    public int getInterline ()
    {
        return scale.getInterline();
    }

    //--------------------//
    // getLocationService //
    //--------------------//
    /**
     * Report the sheet selection service (for LocationEvent & PixelLevelEvent)
     *
     * @return the sheet dedicated event service
     */
    public SelectionService getLocationService ()
    {
        return locationService;
    }

    //--------------//
    // getLogPrefix //
    //--------------//
    /**
     * Report the proper prefix to use when logging a message
     *
     * @return the proper prefix
     */
    public String getLogPrefix ()
    {
        if (ScoresManager.isMultiScore()) {
            return "[" + getId() + "] ";
        } else {
            if (score.isMultiPage()) {
                return "[#" + page.getIndex() + "] ";
            } else {
                return "";
            }
        }
    }

    //---------------------//
    // getLongSectionMaxId //
    //---------------------//
    /**
     * Report the id of the last long horizontal section
     *
     * @return the id of the last long horizontal section
     */
    public int getLongSectionMaxId ()
    {
        return lastLongHSectionId;
    }

    //------------------//
    // getMaxForeground //
    //------------------//
    public int getMaxForeground ()
    {
        if (!hasMaxForeground()) {
            maxForeground = getDefaultMaxForeground();
        }

        return maxForeground;
    }

    //---------//
    // getNest //
    //---------//
    /**
     * Report the global nest for glyphs of this sheet
     *
     * @return the nest for glyphs
     */
    public Nest getNest ()
    {
        return nest;
    }

    //---------//
    // getPage //
    //---------//
    /**
     * @return the page
     */
    public Page getPage ()
    {
        return page;
    }

    //------------//
    // getPicture //
    //------------//
    /**
     * Report the picture of this sheet, that is the image to be processed.
     *
     * @return the related picture
     */
    public Picture getPicture ()
    {
        return picture;
    }

    //----------//
    // getScale //
    //----------//
    /**
     * Report the computed scale of this sheet. This drives several processing
     * thresholds.
     *
     * @return the sheet scale
     */
    public Scale getScale ()
    {
        return scale;
    }

    //-----------------//
    // getScaleBuilder //
    //-----------------//
    /**
     * @return the scaleBuilder
     */
    public ScaleBuilder getScaleBuilder ()
    {
        return scaleBuilder;
    }

    //----------------//
    // getGridBuilder //
    //----------------//
    /**
     * @return the gridBuilder
     */
    public GridBuilder getGridBuilder ()
    {
        if (gridBuilder == null) {
            gridBuilder = new GridBuilder(this);
        }

        return gridBuilder;
    }

    //----------//
    // getScore //
    //----------//
    /**
     * Return the eventual Score that gathers in a score the information
     * retrieved from this sheet.
     *
     * @return the related score, or null if not available
     */
    public Score getScore ()
    {
        return score;
    }

    //-----------------//
    // getShapedGlyphs //
    //-----------------//
    /**
     * Report the collection of glyphs whose shape is identical to the provided
     * shape
     *
     * @param shape the imposed shape
     * @return the (perhaps empty) collection of active glyphs with right shape
     */
    public Collection<Glyph> getShapedGlyphs (Shape shape)
    {
        List<Glyph> found = new ArrayList<>();

        for (Glyph glyph : getActiveGlyphs()) {
            if (glyph.getShape() == shape) {
                found.add(glyph);
            }
        }

        return found;
    }

    //---------//
    // getSkew //
    //---------//
    /**
     * Report the skew information for this sheet. If not yet available,
     * processing is launched to compute the average skew in the sheet image.
     *
     * @return the skew information
     */
    public Skew getSkew ()
    {
        return skew;
    }

    //-----------------//
    // getStaffManager //
    //-----------------//
    /**
     * @return the staffManager
     */
    public StaffManager getStaffManager ()
    {
        return staffManager;
    }

    //----------------------//
    // getSymbolsController //
    //----------------------//
    /**
     * Give access to the module dealing with symbol management
     *
     * @return the symbols model
     */
    public SymbolsController getSymbolsController ()
    {
        if (symbolsController == null) {
            createSymbolsControllerAndEditor();
        }

        return symbolsController;
    }

    //------------------//
    // getSymbolsEditor //
    //------------------//
    /**
     * Give access to the UI dealing with symbol recognition
     *
     * @return the symbols symbolsEditor
     */
    public SymbolsEditor getSymbolsEditor ()
    {
        return symbolsEditor;
    }

    //---------------//
    // getSystemById //
    //---------------//
    /**
     * Report the system info for which id is provided
     *
     * @param id id of desired system
     * @return the desired system info
     */
    public SystemInfo getSystemById (int id)
    {
        return systems.get(id - 1);
    }

    //------------------//
    // getSystemManager //
    //------------------//
    /**
     * @return the systemManager
     */
    public SystemManager getSystemManager ()
    {
        return systemManager;
    }

    //-------------//
    // getSystemOf //
    //-------------//
    /**
     * Report the system info that contains the provided point
     *
     * @param point the provided pixel point
     * @return the containing system info
     *         (or null if there is no enclosing system)
     */
    public SystemInfo getSystemOf (PixelPoint point)
    {
        for (SystemInfo info : getSystems()) {
            SystemBoundary boundary = info.getBoundary();

            if ((boundary != null) && boundary.contains(point)) {
                return info;
            }
        }

        return null;
    }

    //-------------//
    // getSystemOf //
    //-------------//
    /**
     * Report the system, if any, which contains the provided glyph
     * (as determined by the first section of the glyph)
     *
     * @param glyph the provided glyph
     * @return the containing system, or null
     */
    public SystemInfo getSystemOf (Glyph glyph)
    {
        if (glyph.isVirtual() || glyph.getMembers().isEmpty()) {
            return getSystemOf(glyph.getAreaCenter());
        } else {
            return glyph.getMembers().first().getSystem();
        }
    }

    //-------------//
    // getSystemOf //
    //-------------//
    /**
     * Report the system, if any, which contains the provided vertical section
     *
     * @param section the provided section
     * @return the containing system, or null
     */
    public SystemInfo getSystemOf (Section section)
    {
        return section.getSystem();
    }

    //-------------//
    // getSystemOf //
    //-------------//
    /**
     * Report the system that contains ALL glyphs provided.
     * If all glyphs do not belong to the same system, exception is thrown
     *
     * @param glyphs the collection of glyphs
     * @return the containing system
     * @exception IllegalArgumentException raised if glyphs collection is not OK
     */
    public SystemInfo getSystemOf (Collection<Glyph> glyphs)
    {
        if ((glyphs == null) || glyphs.isEmpty()) {
            throw new IllegalArgumentException(
                    "getSystemOf. Glyphs collection is null or empty");
        }

        SystemInfo system = null;
        Collection<Glyph> toRemove = new ArrayList<>();

        for (Glyph glyph : glyphs) {
            SystemInfo glyphSystem = glyph.isVirtual()
                                     ? getSystemOf(glyph.getAreaCenter())
                                     : getSystemOf(glyph);

            if (glyphSystem == null) {
                toRemove.add(glyph);
            } else {
                if (system == null) {
                    system = glyphSystem;
                } else {
                    // Make sure we are still in the same system
                    if (glyphSystem != system) {
                        throw new IllegalArgumentException(
                                "getSystemOf. Glyphs from different systems ("
                                + getSystemOf(glyph) + " and " + system + ") "
                                + Glyphs.toString(glyphs));
                    }
                }
            }
        }

        if (!toRemove.isEmpty()) {
            logger.warning("No system for {0}", Glyphs.toString(toRemove));
            glyphs.removeAll(toRemove);
        }

        return system;
    }

    //---------------------//
    // getSystemOfSections //
    //---------------------//
    /**
     * Report the system that contains ALL sections provided.
     * If all sections do not belong to the same system, exception is thrown
     *
     * @param sections the collection of sections
     * @return the containing system
     * @exception IllegalArgumentException raised if section collection is not
     *                                     OK
     */
    public SystemInfo getSystemOfSections (Collection<Section> sections)
    {
        if ((sections == null) || sections.isEmpty()) {
            throw new IllegalArgumentException(
                    "getSystemOfSections. Sections collection is null or empty");
        }

        SystemInfo system = null;
        Collection<Section> toRemove = new ArrayList<>();

        for (Section section : sections) {
            SystemInfo sectionSystem = section.getSystem();

            if (sectionSystem == null) {
                toRemove.add(section);
            } else {
                if (system == null) {
                    system = sectionSystem;
                } else {
                    // Make sure we are still in the same system
                    if (sectionSystem != system) {
                        throw new IllegalArgumentException(
                                "getSystemOfSections. Sections from different systems ("
                                + section.getSystem() + " and " + system + ") "
                                + Sections.toString(sections));
                    }
                }
            }
        }

        if (!toRemove.isEmpty()) {
            logger.warning("No system for {0}", Sections.toString(toRemove));
            sections.removeAll(toRemove);
        }

        return system;
    }

    //------------//
    // getSystems //
    //------------//
    /**
     * Report the retrieved systems (infos)
     *
     * @return the list of SystemInfo's
     */
    public List<SystemInfo> getSystems ()
    {
        return systems;
    }

    //-------------------//
    // getSystemsBuilder //
    //-------------------//
    /**
     * Give access to the builder in charge of bars & systems computation
     *
     * @return the builder instance
     */
    public SystemsBuilder getSystemsBuilder ()
    {
        return systemsBuilder;
    }

    //----------------//
    // getSystemsNear //
    //----------------//
    /**
     * Report the ordered list of systems containing or close to the provided
     * point
     *
     * @param point the provided point
     * @return a collection of systems ordered by increasing distance from the
     *         provided point
     */
    public List<SystemInfo> getSystemsNear (final Point point)
    {
        List<SystemInfo> neighbors = new ArrayList<>(systems);
        Collections.sort(
                neighbors,
                new Comparator<SystemInfo>()
                {
                    @Override
                    public int compare (SystemInfo s1,
                                        SystemInfo s2)
                    {
                        int y1 = (s1.getTop() + s1.getBottom()) / 2;
                        int d1 = Math.abs(point.y - y1);
                        int y2 = (s2.getTop() + s2.getBottom()) / 2;
                        int d2 = Math.abs(point.y - y2);

                        return Integer.signum(d1 - d2);
                    }
                });

        return neighbors;
    }

    //------------------//
    // getTargetBuilder //
    //------------------//
    /**
     * @return the targetBuilder
     */
    public TargetBuilder getTargetBuilder ()
    {
        return targetBuilder;
    }

    //----------------//
    // getVerticalLag //
    //----------------//
    /**
     * Report the current vertical lag of the sheet
     *
     * @return the current vertical lag
     */
    public Lag getVerticalLag ()
    {
        return vLag;
    }

    //------------------------//
    // getVerticalsController //
    //------------------------//
    public VerticalsController getVerticalsController ()
    {
        return verticalsController;
    }

    //----------//
    // getWidth //
    //----------//
    /**
     * Report the picture width in pixels
     *
     * @return the picture width
     */
    public int getWidth ()
    {
        return picture.getWidth();
    }

    //---------------//
    // hasHistoRatio //
    //---------------//
    /**
     * Check whether the parameter histoRatio has a value
     *
     * @return true if so
     */
    public boolean hasHistoRatio ()
    {
        return histoRatio != null;
    }

    //------------------//
    // hasMaxForeground //
    //------------------//
    public boolean hasMaxForeground ()
    {
        return maxForeground != null;
    }

    //---------------------//
    // hasSystemBoundaries //
    //---------------------//
    /**
     * Report whether the systems have their boundaries defined yet.
     *
     * @return true if already defined
     */
    public boolean hasSystemBoundaries ()
    {
        return hasSystemBoundaries;
    }

    //--------//
    // isDone //
    //--------//
    /**
     * Report whether the specified step has been performed onn this sheet
     *
     * @param step the step to check
     * @return true if already performed
     */
    public boolean isDone (Step step)
    {
        return doneSteps.contains(step);
    }

    //--------------//
    // isOnPatterns //
    //--------------//
    /**
     * Check whether current step is SYMBOLS.
     *
     * @return true if on SYMBOLS
     */
    public boolean isOnPatterns ()
    {
        return Stepping.getLatestStep(this) == Steps.valueOf(Steps.SYMBOLS);
    }

    //--------//
    // remove //
    //--------//
    /**
     * Remove this sheet from the containing score
     */
    public void remove (boolean closing)
    {
        logger.fine("remove sheet {0} closing:{1}", new Object[]{this, closing});

        // Close related UI assembly if any
        if (assembly != null) {
            SheetsController.getInstance().removeAssembly(this);
            assembly.close();
        }

        // Close the related page
        getScore().remove(page);

        if (picture != null) {
            picture.close();
        }

        // If no sheet is left, force score closing
        if (!closing && score.getPages().isEmpty()) {
            score.close();
        }
    }

    //-------------//
    // setAssembly //
    //-------------//
    /**
     * Remember the link to the related sheet display assembly
     *
     * @param assembly the related sheet assembly
     */
    public void setAssembly (SheetAssembly assembly)
    {
        this.assembly = assembly;
    }

    //----------------//
    // setBarsChecker //
    //----------------//
    /**
     * @param barsChecker the barsChecker
     */
    public void setBarsChecker (BarsChecker barsChecker)
    {
        this.barsChecker = barsChecker;
    }

    //-------------------//
    // setBoundaryEditor //
    //-------------------//
    /**
     * @param boundaryEditor the boundaryEditor to set
     */
    public void setBoundaryEditor (BoundaryEditor boundaryEditor)
    {
        this.boundaryEditor = boundaryEditor;
    }

    //----------------//
    // setCurrentStep //
    //----------------//
    public void setCurrentStep (Step step)
    {
        currentStep = step;
    }

    //-------------------------//
    // setDefaultMaxForeground //
    //-------------------------//
    public static void setDefaultMaxForeground (int level)
    {
        constants.maxForegroundGrayLevel.setValue(level);
    }

    //---------------//
    // setHistoRatio //
    //---------------//
    /**
     * Set the sheet value of histogram threhold for staff detection
     *
     * @param histoRatio the ratio of maximum histogram value
     */
    public void setHistoRatio (double histoRatio)
    {
        this.histoRatio = histoRatio;
    }

    //------------------//
    // setHorizontalLag //
    //------------------//
    /**
     * Assign the current horizontal lag for the sheet
     *
     * @param hLag the horizontal lag at hand
     */
    public void setHorizontalLag (Lag hLag)
    {
        this.hLag = hLag;
        hLag.setServices(locationService, nest.getGlyphService());
    }

    //----------------//
    // setHorizontals //
    //----------------//
    /**
     * Set horizontals system by system
     *
     * @param horizontals the horizontals found
     */
    public void setHorizontals (Horizontals horizontals)
    {
        this.horizontals = horizontals;
    }

    //----------//
    // setImage //
    //----------//
    public final void setImage (RenderedImage image)
            throws StepException
    {
        // Reset most of all members
        reset();

        // Beware: Glyph nest must subscribe to location before any lag,
        // to allow cleaning up of glyph data, before publication by a lag
        nest = new BasicNest("gNest", this);
        nest.setServices(locationService);
        
        scaleBuilder = new ScaleBuilder(this);
        
        try {
            picture = new Picture(image, locationService);

            if (picture.getImplicitForeground() == null) {
                picture.setMaxForeground(getMaxForeground());
            }

            setPicture(picture);
            getBench().recordImageDimension(picture.getWidth(), picture.
                    getHeight());

            done(Steps.valueOf(Steps.LOAD));
        } catch (ImageFormatException ex) {
            String msg = "Unsupported image format in file "
                    + getScore().getImagePath() + "\n" + ex.getMessage();

            if (Main.getGui() != null) {
                Main.getGui().displayWarning(msg);
            } else {
                logger.warning(msg);
            }

            throw new StepException(ex);
        }
    }

    //---------------------//
    // setLongSectionMaxId //
    //---------------------//
    /**
     * Remember the id of the last long horizontal section
     *
     * @param id the id of the last long horizontal section
     */
    public void setLongSectionMaxId (int id)
    {
        lastLongHSectionId = id;
    }

    //------------------//
    // setMaxForeground //
    //------------------//
    public void setMaxForeground (int level)
    {
        this.maxForeground = level;
    }

    //----------//
    // setScale //
    //----------//
    /**
     * Link scale information to this sheet
     *
     * @param scale the computed (or read from score file) scale
     * @throws StepException
     */
    public void setScale (Scale scale)
            throws StepException
    {
        this.scale = scale;
        page.setScale(scale);
    }

    //---------//
    // setSkew //
    //---------//
    /**
     * Link skew information to this sheet
     *
     * @param skew the skew information
     */
    public void setSkew (Skew skew)
    {
        this.skew = skew;

        // Update displayed image if any
        if (getPicture().isRotated() && (Main.getGui() != null)) {
            assembly.getComponent().repaint();
        }
    }

    //---------------------//
    // setSystemBoundaries //
    //---------------------//
    /**
     * Set the flag about systems boundaries.
     */
    public void setSystemBoundaries ()
    {
        hasSystemBoundaries = true;
    }

    //------------------//
    // setTargetBuilder //
    //------------------//
    /**
     * @param targetBuilder the targetBuilder to set
     */
    public void setTargetBuilder (TargetBuilder targetBuilder)
    {
        this.targetBuilder = targetBuilder;
    }

    //----------------//
    // setVerticalLag //
    //----------------//
    /**
     * Assign the current vertical lag for the sheet
     *
     * @param vLag the current vertical lag
     * @return the previous vLag, or null
     */
    public Lag setVerticalLag (Lag vLag)
    {
        Lag old = this.vLag;
        this.vLag = vLag;
        vLag.setServices(locationService, nest.getGlyphService());

        return old;
    }

    //----------------//
    // splitBarSticks //
    //----------------//
    /**
     * Split the bar sticks among systems
     *
     * @param barSticks the collection of all bar sticks
     * @return the set of modified systems
     */
    public Set<SystemInfo> splitBarSticks (Collection<? extends Glyph> barSticks)
    {
        Set<SystemInfo> modified = new LinkedHashSet<>();
        Map<SystemInfo, SortedSet<Glyph>> glyphs = new HashMap<>();

        for (SystemInfo system : systems) {
            glyphs.put(
                    system,
                    new ConcurrentSkipListSet<>(system.getGlyphs()));
            system.clearGlyphs();
        }

        // Assign the bar sticks to the proper system glyphs collection
        for (Glyph stick : barSticks) {
            if (stick.isActive()) {
                SystemInfo system = getSystemOf(stick);

                if (system != null) {
                    system.addGlyph(stick);
                }
            }
        }

        for (SystemInfo system : systems) {
            if (!(system.getGlyphs().equals(glyphs.get(system)))) {
                modified.add(system);
            }
        }

        return modified;
    }

    //-------------------------//
    // splitHorizontalSections //
    //-------------------------//
    /**
     * Split the various horizontal sections among systems
     *
     * @return the set of modified systems
     */
    public Set<SystemInfo> splitHorizontalSections ()
    {
        Set<SystemInfo> modifiedSystems = new LinkedHashSet<>();
        Map<SystemInfo, Collection<Section>> sections = new HashMap<>();

        for (SystemInfo system : systems) {
            Collection<Section> systemSections = system.
                    getMutableHorizontalSections();
            sections.put(system, new ArrayList<>(systemSections));
            systemSections.clear();
        }

        for (Section section : getHorizontalLag().getSections()) {
            SystemInfo system = getSystemOf(section.getCentroid());
            // Link section -> system
            section.setSystem(system);

            if (system != null) {
                // Link system <>-> section
                system.getMutableHorizontalSections().add(section);
            }
        }

        for (SystemInfo system : systems) {
            if (!(system.getMutableHorizontalSections().equals(
                  sections.get(system)))) {
                modifiedSystems.add(system);
            }
        }

        return modifiedSystems;
    }

    //-----------------------//
    // splitVerticalSections //
    //-----------------------//
    /**
     * Split the various vertical sections among systems
     *
     * @return the set of modified systems
     */
    public Set<SystemInfo> splitVerticalSections ()
    {
        Set<SystemInfo> modifiedSystems = new LinkedHashSet<>();
        Map<SystemInfo, Collection<Section>> sections = new HashMap<>();

        for (SystemInfo system : systems) {
            Collection<Section> systemSections = system.
                    getMutableVerticalSections();
            sections.put(system, new ArrayList<>(systemSections));
            systemSections.clear();
        }

        for (Section section : getVerticalLag().getSections()) {
            SystemInfo system = getSystemOf(section.getCentroid());
            // Link section -> system
            section.setSystem(system);

            if (system != null) {
                // Link system <>-> section
                system.getMutableVerticalSections().add(section);
            }
        }

        for (SystemInfo system : systems) {
            if (!(system.getMutableVerticalSections().equals(
                  sections.get(system)))) {
                modifiedSystems.add(system);
            }
        }

        return modifiedSystems;
    }

    //----------//
    // toString //
    //----------//
    @Override
    public String toString ()
    {
        return "{Sheet " + page.getId() + "}";
    }

    //-------//
    // reset //
    //-------//
    /**
     * Reinitialize all sheet members
     */
    private void reset ()
    {
        picture = null;
        scale = null;
        skew = null;
        horizontals = null;
        hLag = null;
        vLag = null;
        nest = null;
        
        scaleBuilder = null;
        staffManager.reset();
        gridBuilder = null;
        systemManager.reset();
        systemsBuilder = null;
        symbolsController = null;
        verticalsController = null;
        symbolsEditor = null;
        maxForeground = null;
        histoRatio = null;
        currentStep = null;
        doneSteps = new HashSet<>();
        
    }

    //------------//
    // setPicture //
    //------------//
    /**
     * Set the picture of this sheet, that is the image to be processed.
     *
     * @param picture the related picture
     */
    private void setPicture (Picture picture)
    {
        this.picture = picture;

        locationService.subscribeStrongly(LocationEvent.class, picture);

        // Display sheet picture if not batch mode
        if (Main.getGui() != null) {
            PictureView pictureView = new PictureView(Sheet.this);
            assembly.addViewTab(
                    Step.PICTURE_TAB,
                    pictureView,
                    new BoardsPane(new PixelBoard(Sheet.this)));
        }
    }

    //~ Inner Classes ----------------------------------------------------------
    //-----------//
    // Constants //
    //-----------//
    private static final class Constants
            extends ConstantSet
    {
        //~ Instance fields ----------------------------------------------------

        Constant.Integer maxForegroundGrayLevel = new Constant.Integer(
                "ByteLevel",
                140,
                "Maximum gray level for a pixel to be considered as foreground (black)");
    }
}
