# language: tr

@Cart
Özellik: Kullanici icin alisveris sepeti

  Senaryo: 1 - Kullanici sepetine yeni bir urun eklemek istiyor
    Diyelim ki Kullanici bos olan sepetine sectigi bir urunden 5 adet eklemek istiyor
    O zaman Sistem kullanici icin bir sepet olusturur ve sepetine ilgili urunden 5 adet ekler

  Senaryo: 2 - Kullanici sepetine yeni bir urun eklemek istiyor
    Diyelim ki Kullanici icerisinde urunler olan sepetine sectigi bir urunden 5 adet eklemek istiyor
    Eğer ki Ilgili urun daha onceden sepete eklenmediyse
    O zaman Sistem kullanicinin sepetine ilgili urunden 5 adet ekler

  Senaryo: 3 - Kullanici sepetine yeni bir urun eklemek istiyor
    Diyelim ki Kullanici sepetinde zaten olan bir urunden sepetine 5 adet eklemek istiyor
    O zaman Sistem kullanicinin sepetindeki ilgili urununun sayisini 5 artirir


  Senaryo: 4 - Sepet icin teslimat ucreti hesaplanmasi
    Diyelim ki Kullanicinin sepetindeki urunler icin teslimat ucreti hesaplaniyor
    Eğer ki Kullanicinin sepetinde en az 1 urun var ise
    O zaman Sistem kullanicinin sepeti icin teslimat ucretini hesaplar

  Senaryo: 5 - Sepet icin kategori indirimlerinin hesaplanmasi
    Diyelim ki Kullanicinin sepetindeki urunler icin kategori indirimleri hesaplaniyor
    Eğer ki Kategori icin birden fazla kampanya tanimli ise
    O zaman Sistem kullanicinin sepeti icin mumkun olan en iyi kategori indirimini hesaplar

  Senaryo: 6 - Sepeti icin kullanici kupon aktif ederse olacak durum
    Diyelim ki Kullanicinin elinde bir indirim kuponu var
    Eğer ki Kullanici bu kuponu kullanmak isterse
    O zaman Sistem ilk once kullanici sepeti icin gerekli diger indirimleri hesaplar
    Eğer ki Hesaplanan deger kuponu kullanmak icin gerekli olan asgari degere esit veya buyuk ise
    O zaman Sistem kullanicinin sepeti icin kupon degeri kadar indirimi uygular






