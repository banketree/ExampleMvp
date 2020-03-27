package com.example.base_fun.presenter.glide


class GlidePaletteHelper {
    /**
     * 设置图片主色调
     *
     * @param bitmap
     * @return
     */
//    fun setPaletteColor(bitmap: Bitmap?, view: View) {
//        if (bitmap == null) {
//            return
//        }
//        Palette.from(bitmap).maximumColorCount(10).generate(object : PaletteAsyncListener() {
//            fun onGenerated(@NonNull palette: Palette) { //                List<Palette.Swatch> list = palette.getSwatches();
////                int colorSize = 0;
////                Palette.Swatch maxSwatch = null;
////                for (int i = 0; i < list.size(); i++) {
////                    Palette.Swatch swatch = list.get(i);
////                    if (swatch != null) {
////                        int population = swatch.getPopulation();
////                        if (colorSize < population) {
////                            colorSize = population;
////                            maxSwatch = swatch;
////                        }
////                    }
////                }
//                val s: Palette.Swatch = palette.getDominantSwatch() //独特的一种
//                val s1: Palette.Swatch = palette.getVibrantSwatch() //获取到充满活力的这种色调
//                val s2: Palette.Swatch = palette.getDarkVibrantSwatch() //获取充满活力的黑
//                val s3: Palette.Swatch = palette.getLightVibrantSwatch() //获取充满活力的亮
//                val s4: Palette.Swatch = palette.getMutedSwatch() //获取柔和的色调
//                val s5: Palette.Swatch = palette.getDarkMutedSwatch() //获取柔和的黑
//                val s6: Palette.Swatch = palette.getLightMutedSwatch() //获取柔和的亮
//                if (s1 != null) {
//                    view.setBackgroundColor(s1.getRgb())
//                }
//            }
//        })
//    }
}