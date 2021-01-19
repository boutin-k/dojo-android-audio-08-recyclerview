# RecyclerView

## But du TD
Dans le TD précédent, nous avons parser le JSON se trouvant l'archive OBB. Nous avons désormais accès aux informations liées aux chansons se trouvant dans l'archive.
Dans ce TD, nous allons afficher les chansons dans une liste déroulante.

* Tu peux reprendre ton projet OBB ou alors tu peux récupérer une version [ici](https://github.com/WildCodeSchool/dojo-android-audio-json).


## Etapes
### Créer un Fragment
Comme les Fragments n'ont plus de secret pour nous, nous allons créer un fragment en faisant:
1. Clique droit depuis l'onglet *Project*.
2. New > Fragment > Fragment (List).
3. Modifie le nom du package de destination afin que les sources se trouvent dans ```<package_name>.list```.
4. Clique sur Finish.

### Design de l'Item
* Tu vas modifier le contenu du fragment_item.xml pour qu'il contienne le design suivant:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal">

    <ImageView
        android:id="@+id/item_image"
        android:layout_width="40dp"
        android:layout_height="40dp"
        android:layout_margin="10dp"
        android:contentDescription="@string/image_description"/>

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingLeft="20dp"
        android:paddingStart="20dp"
        android:paddingRight="20dp"
        android:paddingEnd="20dp"
        android:layout_gravity="center_vertical"
        android:orientation="vertical">

        <TextView
            android:id="@+id/item_artist"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="20sp"
            android:textColor="#BBBBBB"/>

        <TextView
            android:id="@+id/item_title"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:textSize="16sp"
            android:textColor="#999999"/>

    </LinearLayout>

</LinearLayout>
```

### Activity Layout
* Tu vas également modifer le layout de l'activité afin d'intégrer le Fragment.
* Voici le xml:
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/state_text_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginBottom="10dp"
        android:textSize="20sp"
        android:textAlignment="center" />

    <fragment
        android:id="@+id/item_fragment"
        android:name="fr.wildcodeschool.dojo_android_obb.list.ItemFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</LinearLayout>
```

### Remplir le RecyclerView
* Tu vas afficher les données du *Singleton* JsonParser dans le RecyclerView **proprement**.
* Le résultat doit ressembler à ceci:

![](http://images.innoveduc.fr/android/dojo-android-audio-recyclerview-screenshot.png)

## Documentation
* [Create a List with RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview)
* [RecyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView)

## TD suivant
* [Playlist](https://github.com/boutin-k/dojo-android-audio-09-playlist)
