function isMobile()
{
  return (navigator.userAgent.match(/Android/i) ||
          navigator.userAgent.match(/webOS/i)   ||
          navigator.userAgent.match(/iPhone/i)  ||
          navigator.userAgent.match(/iPod/i)    ||
          navigator.userAgent.match(/iPad/i));
}

$(document).ready(function()
{
  // show/hide "top" link:
  if(!isMobile())
  {
    $(window).scroll(function()
    {
      if($(this).scrollTop() > 100)
      {
        $('.back-to-top').fadeIn();
      }
      else
      {
        $('.back-to-top').fadeOut();
      }
    });

    $('.content').append('<a href="#" class="back-to-top">top</a>');
    $('.back-to-top').click(function()
    {
      $('html body').animate({scrollTop: 0}, 500);
    });
  }

  // update link targets:
  $('a[href^="http"]').attr('target', '_blank');
  $('a[href^="https"]').attr('target', '_blank');

  // set JS-enabled resources:
  $('a[href$="/nojs"]').each(function()
  {
    var href = $(this).attr('href');

    href = href.substr(0, href.length - 5);

    $(this).attr('href', '/webcam');
  });
});
