package app.parviz.com.simpleyoutubedownloader.loadvideoinfo.viewmodel

/**
 * Load video info video model
 */
data class LoadVideoInfoViewModel(var videoLink : List<String?>,
                                  var videoFormat : List<String?>,
                                  var videoQuality : List<String?>)