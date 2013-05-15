$(".switch_entete").click(function () {
      $(this).toggleClass("entete_ferme");
    });
	    $(".switch_entete").click(function () {
      $(this).toggleClass("entete_open");
    });
$(".formulaire_normal:first").fadeToggle("fast", "linear");
$(".switch_entete:first").click(function() {
  $(".formulaire_normal:first").fadeToggle("fast", "linear");
});
$(".switch_infos_new_collaborateurs").click(function() {
  $(".infos_new_collaborateurs").fadeToggle("fast", "linear");
});

$(".switch_entete2").click(function() {
  $(".infos_collab").fadeToggle("fast", "linear");
});
$(".switch_entete3").click(function() {
  $(".cachee").fadeToggle("fast", "linear");
});
$(".switch_facturation").click(function() {
  $(".facturation_cachee").fadeToggle("fast", "linear");
});