package summerVocation.gitpack.model

import java.io.Serializable

data class SearchUser(var imageurl: String?,
                      var name: String?,
                      var lastcommit: Int?,
                      var createdAt: String?,
                      var follower: Int?

                      )
                     : Serializable {

}
