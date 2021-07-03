# library-dot

## Overview

A library to generate dot file using [DOT Grammar](https://graphviz.gitlab.io/_pages/doc/info/lang.html).

## Usage

### Generate dot file

Import using :

```XML
<properties>
    <frtu-libs.version>LATEST_VERSION</frtu-libs.version>
</properties>

<dependency>
  <groupId>com.github.frtu.libs</groupId>
  <artifactId>lib-dot</artifactId>
  <version>${frtu-libs.version}</version>
</dependency>
```

Java API :

* Generate a Graph
* Add nodes
* Add edges
* Generate a SuperGraph (a Graph of Graph)

```Java
SuperGraph superGraph = new SuperGraph("G");
superGraph.setRankdir("LR");

//--------------------------------------
// Subgraph : cluster_0
//--------------------------------------
final Graph cluster_0 = superGraph.newSubgraph("cluster_0");
cluster_0.newGraphAttributes()
        .setStyle("filled")
        .setColor("lightgrey");
cluster_0.newNodeAttributes()
        .setStyle("filled")
        .setColor("white");

// Short syntax
cluster_0.addEdge("a0", "a1", "a3");

//--------------------------------------
// Subgraph : cluster_1
//--------------------------------------
final Graph cluster_1 = superGraph.newSubgraph("cluster_1");
cluster_1.newEdgeAttributes().setColor("red");
cluster_1.addEdge("b0", "b1", "b2", "b3");

//--------------------------------------
// Super graph itself
//--------------------------------------
final GraphNode start = superGraph.addSingleNode("start", PolygonShapeDotEnum.MDIAMOND);
final GraphNode end = superGraph.addSingleNode("end", PolygonShapeDotEnum.MSQUARE);

// Long syntax
superGraph.addEdge(start, "a0").setStyle("dotted");
superGraph.addEdge(start, "b0");
superGraph.addEdge("a1", "b3");
superGraph.addEdge("b2", "a3").setColor("red");
superGraph.addEdge("a3", "a0");
superGraph.addEdge("a3", end);
superGraph.addEdge("b3", end).setStyle("dotted").setColor("blue");

//--------------------------------------
// Render
//--------------------------------------
final DotRenderer dotRenderer = new DotRenderer();
// boolean : directed graphs or undirected graphs
final String renderGraph = dotRenderer.renderGraph(graph, true);
```

### Visualize dot file

#### Online tool

* [https://dreampuf.github.io/GraphvizOnline/](https://dreampuf.github.io/GraphvizOnline/)

#### UI tools

VSCode and search for Extensions

* Graphviz Interactive Preview : Interactive preview and search
* Graphviz (dot) language support for Visual Studio Code : Add visualizaton pane using ```Cmd+Shift+v```
* Graphviz Markdown Preview : Allow to see graphviz notation inside Markdown



#### Command line tools - GraphViz

* GraphViz (multiple library installable with brew, apt, ...) : [https://www.graphviz.org/download/](https://www.graphviz.org/download/)

For MacOS, just run ```brew install graphviz```, then run :

```
graphviz user.dot
```

You can find the png file in the same folder.

=> Also check [other viewers](https://www.graphviz.org/about/#viewers)

## History

* Migrated from [governance-toolbox](https://github.com/frtu/governance-toolbox/tree/master/libraries/library-dot).
* Existed under :

```XML
<dependency>
  <groupId>com.github.frtu.governance</groupId>
  <artifactId>library-dot</artifactId>
  <version>${governance-libraries.version}</version>
</dependency>
```

## Release notes

### 1.1.2-SNAPSHOT

* 