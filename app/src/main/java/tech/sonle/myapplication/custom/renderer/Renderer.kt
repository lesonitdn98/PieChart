package tech.sonle.myapplication.custom.renderer

import tech.sonle.myapplication.custom.utils.ViewPortHandler

/**
 * Abstract baseclass of all Renderers.
 *
 * Create by SonLe on 19/04/2022
 */
abstract class Renderer(viewPortHandler: ViewPortHandler) {
    /**
     * the component that handles the drawing area of the chart and it's offsets
     */
    protected var mViewPortHandler: ViewPortHandler = viewPortHandler
}