package com.dvm.appd.oasis.dbg.more.adapters

import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dvm.appd.oasis.dbg.R
import com.google.android.play.core.internal.r
import kotlinx.android.synthetic.main.card_credits.view.*

class CreditsAdapter : RecyclerView.Adapter<CreditsAdapter.CreditsAdapterViewHolder>() {
    val credids: List<String> = listOf("com.tbuonomo:morph-bottom-navigation:1.0.1\n\nLicensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0","com.google.dagger:dagger:2.19\n\nCopyright 2012 The Dagger Authors.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "io.reactivex.rxjava2:rxjava:2.2.9\n\nThe Apache Software License, Version 2.0.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0.txt",
        "io.reactivex.rxjava2:rxandroid:2.1.1\n\nThe Apache Software License, Version 2.0.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0.txt",
        "com.squareup.retrofit2:retrofit:2.4.0\n\nCopyright 2013 Square, Inc.Licensed under the Apache License, Version 2.0 (the Licens);you may not use this file except in compliance with the License.You may obtain a copy of the License at Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "com.google.code.gson:gson:2.8.5\n\nCopyright 2008 Google Inc.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "de.hdodenhof:circleimageview:3.0.0\n\nCopyright 2014 - 2019 Henning Dodenhof.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "com.journeyapps:zxing-android-embedded:3.6.0@aar\n\nCopyright (C) 2012-2018 ZXing authors, Journey Mobile.Licensed under the Apache License, Version 2.0 (the License);Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "com.ogaclejapan.smarttablayout:library:1.2.1@aar\n\nCopyright (C) 2015 ogaclejapan.Copyright (C) 2013 The Android Open Source Project.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "com.github.bumptech.glide:glide:4.9.0\n\nBSD, part MIT and Apache 2.0.See the LICENSE file for details.\nhttps://github.com/bumptech/glide/blob/master/LICENSE",
        "com.paytm:pgplussdk:1.3.3\n\nThe Apache Software License, Version 2.0.See the LICENSE file for details.\nhttp://www.apache.org/licenses/LICENSE-2.0.txt",
        "com.balysv:material-ripple:1.0.2\n\nCopyright 2015 Balys Valentukevicius.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "com.labo.kaji:fragmentanimations:0.1.1\n\nCopyright 2015 kakajika.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0",
        "com.jakewharton.rxbinding3:rxbinding:3.0.0\n\nCopyright (C) 2015 Jake Wharton.Licensed under the Apache License, Version 2.0 (the License);you may not use this file except in compliance with the License.Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an AS IS BASIS,WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.See the License for the specific language governing permissions and limitations under the License.You may obtain a copy of the License at\nhttp://www.apache.org/licenses/LICENSE-2.0")
    override fun getItemCount(): Int {
        return  credids.size
    }

    override fun onBindViewHolder(holder: CreditsAdapterViewHolder, position: Int) {
        holder.txt.text = credids[position]
        holder.txt.movementMethod = LinkMovementMethod.getInstance()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreditsAdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_credits, parent, false)
        return CreditsAdapterViewHolder(view)
    }


    inner class CreditsAdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txt = view.text_card_credits
    }

}