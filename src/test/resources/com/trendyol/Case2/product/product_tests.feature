# language: tr

@Product
Özellik: Sisteme yeni urun eklenmesi

  Senaryo: 1 - Urun yoneticisinin sisteme basirili bir sekilde yeni bir urun eklemesi
    Diyelim ki Urun yoneticisi sisteme tum gerekli kosullari saglayan yeni bir urun eklemek istiyor
    Eğer ki Urun yoneticisi eklenecek urun icin gecerli bir fiyat bilgisi girdiyse
    Ve Urun yoneticisi eklenek urun icin gecerli bir kategori girdiyse
    O zaman Sistem yeni urunu kaydeder

  Senaryo: 2 - Urun yoneticisi sisteme gerekli kosullari saglamayan bir urun eklemeye calisirsa
    Diyelim ki Urun yoneticisi sisteme gecersiz bir fiyat bilgisi ile yeni bir urun eklemek istiyor
    Eğer ki Urun yoneticisi eklenecek urun icin gecerli bir fiyat bilgisi girmedi ise
    O zaman Sistem urunu kaydetmez ve urun yoneticisine hatali fiyat bilgisi seklinde bir mesaj gonderir

  Senaryo: 3- Urun yoneticisi sisteme gerekli kosullari saglamayan bir urun eklemeye calisirsa
    Diyelim ki Urun yoneticisi sisteme gecersiz bir kategori bilgisine sahip yeni bir urun eklemek istiyor
    Eğer ki Urun yoneticisi eklenecek urun icin gecerli bir kategori girmediyse
    O zaman Sistem urunu kaydetmez ve urun yoneticisine hatali kategori bilgisi seklinde bir mesaj gonderir

#  Senaryo 4 - Urun yoneticisi sisteme gerekli kosullari saglamayan bir urun eklemeye calisirsa
#    Diyelim ki Urun yoneticisi sisteme yeni bir urun eklemek istiyor
#    Eğer ki Urun yoneticisi eklenecek urun icin gecerli bir fiyat bilgisi girdiyse
#    Ve Urun yoneticisi eklenek urun icin gecerli bir kategori girdiyse
#    Fakat Urun yoneticisi eklenecek urun icin gecerli bir stok sayisi girmediyse
#    O zaman Sistem urunu kaydetmez ve urun yoneticisine hatali stok sayisi bilgisi seklinde bir mesaj gonderir



