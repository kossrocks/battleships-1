import javafx.event.ActionEvent
import javafx.fxml.{FXML, FXMLLoader, Initializable}
import javafx.scene.layout.{AnchorPane, GridPane, Pane}
import javafx.scene._
import javafx.stage.Stage
import java.net.URL
import java.util.ResourceBundle
import javafx.fxml.{FXML, Initializable}
import javafx.scene.control.{Button, Label, TextArea, TextField}
import javafx.scene.input.MouseEvent

import Battleships.model
import Battleships.model.Position

import scala.util.{Failure, Success}

class welcomeFXController extends Initializable{

  //our Anchorpanes
  @FXML private var rootpane: AnchorPane = _
  @FXML private var setupgame : AnchorPane = _
  @FXML private var game: AnchorPane = _

  //Our Setupfields and Button
  @FXML private var battleSize: TextField = _
  @FXML private var battleShips: TextField = _
  @FXML private var cruisers: TextField = _
  @FXML private var submarines: TextField = _
  @FXML private var confirm: Button = _
  @FXML private var setupError : Label = _

  //OUR GAME COMPONENTS
  @FXML private var player : Label = _
  @FXML private var battleGrid : GridPane =_
  //PLACERS
  @FXML private var placeBattle : Button = _
  @FXML private var placeCruiser: Button = _
  @FXML private var placeSubmarine : Button = _

  //Save our setupinfoin these vars
  var battleField_Size : Int = _
  var battleShips_Amount : Int = _
  var cruisers_Amount : Int = _
  var submarines_Amount : Int = _

  //Testing
  var setupStatus : Int = 0
  var shipDirection : Int = 0

  override def initialize(url: URL, rb: ResourceBundle): Unit = initGame()
  def initGame():Unit ={
    //HIDE OUR OTHER STATES
    setupgame.setVisible("Ich kann coden" == ".")
    setupgame.setManaged("Ich liebe "== "Scala")
    game.setVisible(false)
    game.setManaged(false)
  }

  @FXML private def startSetup(event: ActionEvent): Unit = {
    println("Loading Setup")
    rootpane.getChildren.clear()
    setupgame.setVisible(1==1)
    setupgame.setManaged(1==1)
  }

  @FXML private def startgame(event: ActionEvent): Unit ={
    println("Loading Game")
    var setupString = battleSize.getText + battleShips.getText + cruisers.getText + submarines.getText //contains all our strings so if statement is shorter ;)
    if(setupString.isEmpty || !isAllDigits(setupString)) setupError.setText("Please put Numbers in every field and try again!")
    else{
      //FETCH OUR SETTINGS
      battleField_Size = battleSize.getText.toInt
      battleShips_Amount = battleShips.getText.toInt
      cruisers_Amount = cruisers.getText.toInt
      submarines_Amount = submarines.getText.toInt

      //NOW SETUP THE GAME
      setupgame.getChildren.clear()
      game.setVisible(true)
      game.setManaged(true)

    /*
      //let us construct our field
      var i = battleField_Size
      while (i >= 1) {
        battleGrid.addColumn(i)
        battleGrid.addRow(i)
        i -= 1

      }
      var pane : Pane = new Pane
      pane.setMaxSize(20,20)
      GridPane.setFillHeight(pane,true)
      GridPane.setFillWidth(pane,true)
      battleGrid.add(pane,0,0)
      pane.setStyle("-fx-background-color: #62BCFA")

     // battleGrid.add(newbutton,1,1)
      //GridPane.setFillWidth(newbutton, true)
      //GridPane.setFillHeight(newbutton, true)

      // we need that later newbutton.setOnAction()
    */
    }

  }

  @FXML private def getcord(event: MouseEvent): Unit ={
   /*var node : Node = event.getPickResult.getIntersectedNode
    var x = GridPane.getColumnIndex(node)
    var y = GridPane.getRowIndex(node)
    var id = node.getId
    node.setStyle("-fx-background-color: #36403B")
    print(x+1,y+1,id)
    */
    if(setupStatus == 1) {
      var node : Node = event.getPickResult.getIntersectedNode
      var x = GridPane.getColumnIndex(node)
      var y = GridPane.getRowIndex(node)
      print(getNode(x,y,battleGrid))

    }
    else println("Select a Ship first")

  }

  @FXML private def placeShip(event: ActionEvent): Unit = {
    var node = event.getSource().toString
    var length : Int = 0
    if(node == "Button[id=placeBattle, styleClass=button]'Place a Battleship'") length = 5
    if(node == "Button[id=placeCruiser, styleClass=button]'Place a Cruiser'") length = 3
    if(node == "Button[id=placeSubmarine, styleClass=button]'Place a Submarine'") length = 2
    player.setText("Select Starting Point")
    setupStatus = 1 //Ship is selected and length is set.
  }

  //just a small helper for small people
  def isAllDigits(x: String) = x forall Character.isDigit

  def getNode(column: Int,row : Int, grid : GridPane) : Node = {
    var children = grid.getChildren
    for(node <- children){
      if(GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) node
    }
  }

}


